package Net.PC15.FC8800.Command.Door;
import Net.PC15.FC8800.Command.Door.Parameter.OpenDoor_Parameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class OpenDoor extends FC8800Command {
	public OpenDoor(OpenDoor_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(5);
		if (par.IsCheckNum) {
			dataBuf.writeByte(par.CheckNum);
		}
		for (int i = 1; i < 5; ++i) {
			dataBuf.writeByte(par.Door.GetDoor(i));
		}
		if (par.IsCheckNum) {
			this.CreatePacket(3, 3, 128, 5, dataBuf);
		}
		else {
			this.CreatePacket(3, 3, 0, 4, dataBuf);
		}
	}
	protected void Release0() {
	}
}
