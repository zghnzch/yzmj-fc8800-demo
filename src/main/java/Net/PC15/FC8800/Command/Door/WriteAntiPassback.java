package Net.PC15.FC8800.Command.Door;
import Net.PC15.FC8800.Command.Door.Parameter.WriteAntiPassback_Parameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WriteAntiPassback extends FC8800Command {
	public WriteAntiPassback(WriteAntiPassback_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(2);
		dataBuf.writeByte(par.DoorNum);
		dataBuf.writeBoolean(par.Use);
		this.CreatePacket(3, 12, 1, 2, dataBuf);
	}
	protected void Release0() {
	}
}
