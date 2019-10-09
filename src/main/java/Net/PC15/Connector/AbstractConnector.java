package Net.PC15.Connector;
import Net.PC15.Command.INCommand;
import Net.PC15.Command.INCommandRuntime;
import Net.PC15.Command.INIdentity;
import Net.PC15.Command.INWatchResponse;
import Net.PC15.Packet.PacketDecompileAllocator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import java.util.Calendar;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
/**
 * @author zch
 */
public abstract class AbstractConnector implements INConnector {
	private final static org.apache.log4j.Logger myLog = org.apache.log4j.Logger.getRootLogger();
	protected ConcurrentLinkedQueue _CommandList = new ConcurrentLinkedQueue();
	protected ConcurrentHashMap _DecompileList = new ConcurrentHashMap();
	protected boolean _IsForcibly = false;
	protected INCommand _ActivityCommand;
	protected E_ConnectorStatus _Status;
	protected INConnectorEvent _Event;
	protected Boolean _isRelease;
	protected Boolean _isInvalid;
	protected Calendar _ActivityDate;
	protected boolean _IsRuning;
	public AbstractConnector() {
		this._Status = E_ConnectorStatus.OnClosed;
		this._isRelease = false;
		this._isInvalid = false;
		this._ActivityDate = Calendar.getInstance();
		this._IsRuning = false;
	}
	@Override
	public boolean IsInvalid() {
		return this._isInvalid;
	}
	protected void UpdateActivityTime() {
		this._ActivityDate = Calendar.getInstance();
	}
	protected void CheckChanelIsInvalid() {
		long lConnectMillis = this._ActivityDate.getTimeInMillis();
		long lNowMillis = Calendar.getInstance().getTimeInMillis();
		long lElapse = lNowMillis - lConnectMillis;
		long InvalidTime = 60000L;
		this._isInvalid = lElapse > InvalidTime;
	}
	@Override
	public int GetCommandCount() {
		return this._CommandList.size();
	}
	@Override
	public synchronized void AddCommand(INCommand cmd) {
		this._CommandList.offer(cmd);
		if (cmd.GetIdentity() != null) {
			INIdentity inIdentity = cmd.GetIdentity();
			E_ControllerType eControllerType = inIdentity.GetIdentityType();
			// TODO
			INWatchResponse inWatchResponse = PacketDecompileAllocator.GetDecompile(eControllerType);
			this.AddWatchDecompile(inWatchResponse);
		}
	}
	@Override
	public synchronized void AddWatchDecompile(INWatchResponse decompile) {
		if (decompile != null) {
			String key = decompile.getClass().getCanonicalName();
			if (!this._DecompileList.containsKey(key)) {
				this._DecompileList.put(key, decompile);
			}
		}
	}
	@Override
	public synchronized void Release() {
		if (!this._isRelease) {
			this._isRelease = true;
			this.Release0();
			this._CommandList.clear();
			this._CommandList = null;
			if (this._DecompileList != null) {
				Iterator var1 = this._DecompileList.values().iterator();
				while (var1.hasNext()) {
					INWatchResponse value = (INWatchResponse) var1.next();
					value.Release();
				}
				this._DecompileList.clear();
				this._DecompileList = null;
			}
			this._ActivityCommand = null;
			this._Status = null;
			this._Event = null;
		}
	}
	protected abstract void Release0();
	@Override
	public boolean IsForciblyConnect() {
		return this._IsForcibly;
	}
	@Override
	public void OpenForciblyConnect() {
		this._IsForcibly = true;
	}
	@Override
	public void CloseForciblyConnect() {
		this._IsForcibly = false;
	}
	@Override
	public void SetEventHandle(INConnectorEvent oEvent) {
		this._Event = oEvent;
	}
	@Override
	public void run() {
		this._IsRuning = true;
		this.CheckStatus();
		this._IsRuning = false;
	}
	protected abstract void CheckStatus();
	@Override
	public E_ConnectorStatus GetStatus() {
		return this._Status;
	}
	protected void RaiseConnectorErrorEvent(INCommand cmd, boolean bIsStopCommand) {
		if (!bIsStopCommand) {
			ConnectorDetail detail = this.GetConnectorDetail();
			if (this._Event != null) {
				this._Event.ConnectorErrorEvent(detail);
			}
		}
		if (cmd != null) {
			INConnectorEvent cmdEvent = cmd.getCommandParameter().getCommandDetail().Event;
			if (this._Event != null) {
				this._Event.ConnectorErrorEvent(cmd, bIsStopCommand);
			}
			if (cmdEvent != null) {
				cmdEvent.ConnectorErrorEvent(cmd, bIsStopCommand);
			}
		}
	}
	protected void RaiseCommandCompleteEventEvent(INCommand cmd) {
		if (this._Event != null) {
			this._Event.CommandCompleteEvent(cmd, null);
		}
		INConnectorEvent cmdEvent = cmd.getCommandParameter().getCommandDetail().Event;
		if (cmdEvent != null) {
			cmdEvent.CommandCompleteEvent(cmd, null);
		}
	}
	protected synchronized void fireConnectError() {
		Object[] cmds = this._CommandList.toArray();
		this._CommandList.clear();
		this._ActivityCommand = null;
		if (cmds.length == 0) {
			this.RaiseConnectorErrorEvent(null, false);
		}
		else {
			Object[] var2 = cmds;
			int var3 = cmds.length;
			for (int var4 = 0; var4 < var3; ++var4) {
				Object obj = var2[var4];
				INCommandRuntime cmd = (INCommandRuntime) obj;
				this.RaiseConnectorErrorEvent((INCommand) cmd, false);
			}
		}
	}
	@Override
	public synchronized void StopCommand(INIdentity idt) {
		Object[] cmds = this._CommandList.toArray();
		Object[] var3;
		int var4;
		int var5;
		Object obj;
		INCommandRuntime cmd;
		if (idt == null) {
			this._CommandList.clear();
			this._ActivityCommand = null;
			var3 = cmds;
			var4 = cmds.length;
			for (var5 = 0; var5 < var4; ++var5) {
				obj = var3[var5];
				cmd = (INCommandRuntime) obj;
				this.RaiseConnectorErrorEvent((INCommand) cmd, true);
			}
		}
		else {
			var3 = cmds;
			var4 = cmds.length;
			for (var5 = 0; var5 < var4; ++var5) {
				obj = var3[var5];
				cmd = (INCommandRuntime) obj;
				INIdentity cmdIdentity = cmd.GetIdentity();
				if (idt.equals(cmdIdentity)) {
					this.RaiseConnectorErrorEvent((INCommand) cmd, true);
					if (cmd == this._ActivityCommand) {
						this._ActivityCommand = null;
					}
				}
			}
		}
	}
	protected abstract ConnectorDetail GetConnectorDetail();
	protected synchronized void CheckWatchResponse(ByteBuf msg) {
		if (this._DecompileList.size() != 0) {
			Iterator var2 = this._DecompileList.values().iterator();
			while (var2.hasNext()) {
				INWatchResponse value = (INWatchResponse) var2.next();
				value.CheckResponse(this.GetConnectorDetail(), this._Event, msg);
				/* ============================== */
				//				byte[] receiveBytes = LogUtil.getBytesFromByteBuf2(msg);
				//				String receiveStr = LogUtil.bytes2HexString(receiveBytes);
				//				myLog.info("收:" + receiveBytes + " " + receiveStr + "  ArraysToString " + Arrays.toString(receiveBytes));
				//				String receiveStr2 = ByteBufUtil.hexDump(msg).toUpperCase();
				//				myLog.info("收2:" + receiveBytes + " " + receiveStr2 + "  ArraysToString " + Arrays.toString(receiveBytes));
				/* ============================== */
				String receiveStr2 = ByteBufUtil.hexDump(msg).toUpperCase();
				myLog.info("收2:" + receiveStr2);
				/* ============================== */
			}
		}
	}
	@Override
	public boolean TaskIsBegin() {
		return this._IsRuning;
	}
	@Override
	public void SetTaskIsBegin() {
		this._IsRuning = true;
	}
}
