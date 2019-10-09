package Net.PC15.FC8800.Command.Door;
import Net.PC15.FC8800.Command.Door.Parameter.WriteInvalidCardAlarmOption_Parameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WriteInvalidCardAlarmOption extends FC8800Command {
	public WriteInvalidCardAlarmOption(WriteInvalidCardAlarmOption_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(2);
		dataBuf.writeByte(par.DoorNum);
		dataBuf.writeBoolean(par.Use);
		this.CreatePacket(3, 10, 0, 2, dataBuf);
	}
	protected void Release0() {
	}
}
