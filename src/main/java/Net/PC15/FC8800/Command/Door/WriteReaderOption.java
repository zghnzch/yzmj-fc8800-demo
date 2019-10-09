package Net.PC15.FC8800.Command.Door;
import Net.PC15.FC8800.Command.Door.Parameter.WriteReaderOption_Parameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WriteReaderOption extends FC8800Command {
	public WriteReaderOption(WriteReaderOption_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(4);
		dataBuf.writeBytes(par.door.DoorPort);
		this.CreatePacket(3, 1, 1, 4, dataBuf);
	}
	protected void Release0() {
	}
}
