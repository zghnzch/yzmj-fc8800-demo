package Net.PC15.FC8800.Command.Door;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Door.Parameter.DoorPort_Parameter;
import Net.PC15.FC8800.Command.Door.Result.ReadAlarmPassword_Result;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class ReadAlarmPassword extends FC8800Command {
	public ReadAlarmPassword(DoorPort_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
		dataBuf.writeByte(par.Door);
		this.CreatePacket(3, 11, 1, 1, dataBuf);
	}
	protected void Release0() {
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 3, 11, 1, 7)) {
			ByteBuf buf = model.GetDatabuff();
			ReadAlarmPassword_Result r = new ReadAlarmPassword_Result();
			r.DoorNum = buf.readByte();
			r.Use = buf.readBoolean();
			byte[] pwd = new byte[4];
			buf.readBytes(pwd, 0, 4);
			r.Password = ByteUtil.ByteToHex(pwd);
			r.AlarmOption = buf.readByte();
			this._Result = r;
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
}
