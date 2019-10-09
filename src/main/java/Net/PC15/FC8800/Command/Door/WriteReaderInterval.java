package Net.PC15.FC8800.Command.Door;
import Net.PC15.FC8800.Command.Door.Parameter.WriteReaderInterval_Parameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WriteReaderInterval extends FC8800Command {
	public WriteReaderInterval(WriteReaderInterval_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(3);
		dataBuf.writeByte(par.DoorNum);
		dataBuf.writeBoolean(par.Use);
		dataBuf.writeByte(par.RecordOption);
		this.CreatePacket(3, 9, 1, 3, dataBuf);
	}
	protected void Release0() {
	}
}
