package Net.PC15.FC8800.Command.TimeGroup;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Data.TimeGroup.WeekTimeGroup;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.TimeGroup.Result.ReadTimeGroup_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
public class ReadTimeGroup extends FC8800Command {
	protected int mStep;
	protected ConcurrentLinkedQueue mBufs;
	protected int mRecordCardSize;
	public ReadTimeGroup(CommandParameter par) {
		this._Parameter = par;
		this.mBufs = new ConcurrentLinkedQueue();
		this.CreatePacket(6, 2);
	}
	protected void Release0() {
		this.ClearBuf();
	}
	protected void ClearBuf() {
		if (this.mBufs != null) {
			while (this.mBufs.peek() != null) {
				ByteBuf buf = (ByteBuf) this.mBufs.poll();
				buf.release();
			}
			this._Result = null;
		}
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		ByteBuf buf;
		int iCardSize;
		if (this.CheckResponse_Cmd(model, 6, 2, 0)) {
			try {
				buf = model.GetDatabuff();
				iCardSize = buf.getInt(0);
				this._ProcessStep += iCardSize;
				this.RaiseCommandProcessEvent(oEvent);
				buf.retain();
				this.mBufs.add(buf);
				this.CommandWaitResponse();
			}
			catch (Exception var6) {
				System.out.println("发送错误：" + var6.getLocalizedMessage());
			}
			return true;
		}
		else if (this.CheckResponse_Cmd(model, 6, 2, 255, 4)) {
			buf = model.GetDatabuff();
			iCardSize = buf.readInt();
			if (iCardSize > 0) {
				this.CommandWaitResponse();
				try {
					this._Result = this.Analysis(iCardSize);
				}
				catch (Exception var7) {
				}
				this.RaiseCommandCompleteEvent(oEvent);
			}
			else {
				this.RaiseCommandCompleteEvent(oEvent);
			}
			return true;
		}
		else {
			return false;
		}
	}
	protected ReadTimeGroup_Result Analysis(int iSize) throws Exception {
		ReadTimeGroup_Result result = new ReadTimeGroup_Result();
		result.DataBaseSize = iSize;
		ArrayList list = new ArrayList(iSize);
		while (this.mBufs.peek() != null) {
			ByteBuf buf = (ByteBuf) this.mBufs.poll();
			WeekTimeGroup wtg = new WeekTimeGroup(8);
			wtg.SetBytes(buf);
			list.add(wtg);
			buf.release();
		}
		ByteBuf var6;
		for (Iterator var7 = this.mBufs.iterator(); var7.hasNext(); var6 = (ByteBuf) var7.next()) {
		}
		result.List = list;
		return result;
	}
}
