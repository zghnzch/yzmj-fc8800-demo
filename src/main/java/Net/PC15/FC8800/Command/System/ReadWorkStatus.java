package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Data.DoorLimit;
import Net.PC15.FC8800.Command.Data.DoorPortDetail;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadWorkStatus_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;
public class ReadWorkStatus extends FC8800Command {
	public ReadWorkStatus(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 14, 0);
	}
	protected void Release0() {
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (!this.CheckResponse_Cmd(model, 1, 14, 0, 52)) {
			return false;
		}
		else {
			ByteBuf buf = model.GetDatabuff();
			ReadWorkStatus_Result ret = new ReadWorkStatus_Result();
			this._Result = ret;
			ret.RelayState = this.GetDoorPortByByteBuf(buf, 4);
			ret.DoorLongOpenState = this.GetDoorPortByByteBuf(buf, 4);
			ret.DoorState = this.GetDoorPortByByteBuf(buf, 4);
			ret.DoorAlarmState = this.GetDoorPortByByteBuf(buf, 4);
			ret.AlarmState = buf.readUnsignedByte();
			ret.LockState = this.GetDoorPortByByteBuf(buf, 8);
			ret.PortLockState = this.GetDoorPortByByteBuf(buf, 4);
			ret.WatchState = buf.readByte();
			DoorLimit dl = new DoorLimit();
			dl.GlobalEnter = buf.readUnsignedInt();
			for (int i = 0; i < 4; ++i) {
				dl.DoorEnter[i] = buf.readUnsignedInt();
			}
			ret.EnterTotal = dl;
			ret.TheftState = buf.readByte();
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
	}
	private DoorPortDetail GetDoorPortByByteBuf(ByteBuf buf, int iDoorCount) {
		byte[] bData = new byte[iDoorCount];
		buf.readBytes(bData, 0, iDoorCount);
		DoorPortDetail dt = new DoorPortDetail((short) iDoorCount);
		dt.DoorPort = bData;
		return dt;
	}
}
