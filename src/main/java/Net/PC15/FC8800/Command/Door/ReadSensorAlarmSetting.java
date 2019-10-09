package Net.PC15.FC8800.Command.Door;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Door.Parameter.DoorPort_Parameter;
import Net.PC15.FC8800.Command.Door.Result.ReadSensorAlarmSetting_Result;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class ReadSensorAlarmSetting extends FC8800Command {
	public ReadSensorAlarmSetting(DoorPort_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
		dataBuf.writeByte(par.Door);
		this.CreatePacket(3, 16, 0, 1, dataBuf);
	}
	protected void Release0() {
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 3, 16, 0, 226)) {
			ByteBuf buf = model.GetDatabuff();
			ReadSensorAlarmSetting_Result r = new ReadSensorAlarmSetting_Result();
			r.DoorNum = buf.readByte();
			r.Use = buf.readBoolean();
			r.TimeGroup.SetBytes(buf);
			this._Result = r;
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
}
