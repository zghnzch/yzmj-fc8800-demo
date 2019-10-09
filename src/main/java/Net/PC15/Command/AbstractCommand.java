package Net.PC15.Command;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.Packet.INPacket;
import Net.PC15.Packet.INPacketDecompile;
import Net.PC15.Packet.INPacketModel;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Calendar;
public abstract class AbstractCommand implements INCommand {
	protected INCommandParameter _Parameter;
	protected INCommandResult _Result;
	protected int _ProcessMax = 1;
	protected int _ProcessStep = 0;
	protected int _SendCount = 0;
	protected boolean _IsCommandOver = false;
	protected Calendar _SendDate = null;
	protected boolean _IsWaitResponse = true;
	protected INPacket _Packet;
	protected INPacketDecompile _Decompile;
	protected E_CommandStatus _Status;
	protected int _ReSendCount;
	protected boolean _IsTimeout;
	protected boolean _IsRelease;
	public AbstractCommand() {
		this._Status = E_CommandStatus.OnReady;
		this._ReSendCount = 0;
		this._IsRelease = false;
	}
	@Override
	public int getProcessMax() {
		return this._ProcessMax;
	}
	@Override
	public int getProcessStep() {
		return this._ProcessStep;
	}
	@Override
	public int getSendCount() {
		return this._SendCount;
	}
	@Override
	public boolean getIsCommandOver() {
		return this._IsCommandOver;
	}
	@Override
	public boolean getIsTimeout() {
		return this._IsTimeout;
	}
	@Override
	public void Release() {
		if (!this._IsRelease) {
			this._IsRelease = true;
			this.Release0();
			this._Parameter = null;
			this._Result = null;
			this._SendDate = null;
			if (this._Packet != null) {
				this._Packet.Release();
			}
			this._Packet = null;
			if (this._Decompile != null) {
				this._Decompile.Release();
			}
			this._Decompile = null;
		}
	}
	protected abstract void Release0();
	@Override
	public INCommandParameter getCommandParameter() {
		return this._Parameter;
	}
	@Override
	public void setCommandParameter(INCommandParameter par) {
		this._Parameter = par;
	}
	@Override
	public INIdentity GetIdentity() {
		return this._Parameter == null ? null : this._Parameter.getCommandDetail().Identity;
	}
	@Override
	public INCommandResult getCommandResult() {
		return this._Result;
	}
	@Override
	public boolean getIsWaitResponse() {
		return this._IsWaitResponse;
	}
	@Override
	public boolean CheckResponse(INConnectorEvent oEvent, ByteBuf bData) {
		if (this._Status != E_CommandStatus.OnWaitResponse) {
			return false;
		}
		else {
			boolean decompile = false;
			synchronized (this) {
				try {
					ArrayList oRetPack = new ArrayList(10);
					decompile = this._Decompile.Decompile(bData, oRetPack);
					if (decompile) {
						int iLen = oRetPack.size();
						for (int i = 0; i < iLen; ++i) {
							INPacketModel model = (INPacketModel) oRetPack.get(i);
							if (this._Status == E_CommandStatus.OnWaitResponse && this.CommandStep(oEvent, model)) {
								this._SendDate = Calendar.getInstance();
							}
							model.Release();
						}
					}
				}
				catch (Exception var10) {
					System.out.println(" AbstractCommand--CheckResponse -- 发生错误：\n" + var10.toString());
					decompile = false;
				}
				return decompile;
			}
		}
	}
	protected abstract boolean CommandStep(INConnectorEvent var1, INPacketModel var2);
	@Override
	public boolean CheckTimeout(INConnectorEvent oEvent) {
		if (this._Status != E_CommandStatus.OnWaitResponse) {
			return false;
		}
		else if (this._SendDate == null) {
			return false;
		}
		else {
			int iTimeout = 1000;
			int iMaxReSendCount = 3;
			CommandDetail detail = null;
			if (this._Parameter != null) {
				detail = this._Parameter.getCommandDetail();
			}
			if (detail != null) {
				iTimeout = this._Parameter.getCommandDetail().Timeout;
				iMaxReSendCount = detail.RestartCount;
			}
			long lSendMillis = this._SendDate.getTimeInMillis();
			long lNowMillis = Calendar.getInstance().getTimeInMillis();
			long lElapse = lNowMillis - lSendMillis;
			boolean bIsTimeout = lElapse > (long) iTimeout;
			if (bIsTimeout) {
				if (this._ReSendCount != iMaxReSendCount) {
					this.CommandOver_ReSend();
					this._Status = E_CommandStatus.OnReady;
					++this._ReSendCount;
					return false;
				}
				else {
					this._IsTimeout = true;
					this.CommandOver();
					this.RaiseCommandTimeout(oEvent);
					return true;
				}
			}
			else {
				return false;
			}
		}
	}
	protected void CommandOver_ReSend() {
	}
	@Override
	public INPacket GetPacket() {
		return this._Packet;
	}
	@Override
	public E_CommandStatus GetStatus() {
		return this._Status;
	}
	protected void CommandReady() {
		this._Status = E_CommandStatus.OnReady;
		this._ReSendCount = 0;
	}
	protected void CommandWaitResponse() {
		this._Status = E_CommandStatus.OnWaitResponse;
		this._SendDate = Calendar.getInstance();
	}
	@Override
	public void SendCommand(INConnectorEvent oEvent) {
		this._Status = E_CommandStatus.OnWaitResponse;
		if (this._SendCount == 0) {
			this._ProcessStep = 1;
		}
		++this._SendCount;
		this._SendDate = Calendar.getInstance();
		this.RaiseCommandProcessEvent(oEvent);
	}
	protected void CommandOver() {
		this._Status = E_CommandStatus.OnOver;
		this._ProcessStep = this._ProcessMax;
		if (this._Packet != null) {
			this._Packet.Release();
		}
		this._Packet = null;
		if (this._Decompile != null) {
			this._Decompile.Release();
		}
		this._Decompile = null;
		this._IsCommandOver = true;
	}
	@Override
	public void RaiseCommandCompleteEvent(INConnectorEvent oEvent) {
		this.CommandOver();
		if (oEvent != null) {
			oEvent.CommandCompleteEvent(this, this._Result);
		}
		INConnectorEvent cmdEvent = this._Parameter.getCommandDetail().Event;
		if (cmdEvent != null) {
			cmdEvent.CommandCompleteEvent(this, this._Result);
		}
	}
	@Override
	public void RaiseCommandProcessEvent(INConnectorEvent oEvent) {
		if (oEvent != null) {
			oEvent.CommandProcessEvent(this);
		}
		INConnectorEvent cmdEvent = this._Parameter.getCommandDetail().Event;
		if (cmdEvent != null) {
			cmdEvent.CommandProcessEvent(this);
		}
	}
	protected void RaiseCommandTimeout(INConnectorEvent oEvent) {
		if (oEvent != null) {
			oEvent.CommandTimeout(this);
		}
		INConnectorEvent cmdEvent = this._Parameter.getCommandDetail().Event;
		if (cmdEvent != null) {
			cmdEvent.CommandTimeout(this);
		}
	}
	protected void RaisePasswordErrorEvent(INConnectorEvent oEvent) {
		if (oEvent != null) {
			oEvent.PasswordErrorEvent(this);
		}
		INConnectorEvent cmdEvent = this._Parameter.getCommandDetail().Event;
		if (cmdEvent != null) {
			cmdEvent.PasswordErrorEvent(this);
		}
	}
	protected void RaiseChecksumErrorEvent(INConnectorEvent oEvent) {
		if (oEvent != null) {
			oEvent.ChecksumErrorEvent(this);
		}
		INConnectorEvent cmdEvent = this._Parameter.getCommandDetail().Event;
		if (cmdEvent != null) {
			cmdEvent.ChecksumErrorEvent(this);
		}
	}
}
