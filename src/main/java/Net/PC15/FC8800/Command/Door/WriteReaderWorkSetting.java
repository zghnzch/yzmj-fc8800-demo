package Net.PC15.FC8800.Command.Door;
import Net.PC15.FC8800.Command.Door.Parameter.WriteReaderWorkSetting_Parameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WriteReaderWorkSetting extends FC8800Command {
	public WriteReaderWorkSetting(WriteReaderWorkSetting_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(281);
		dataBuf.writeByte(par.DoorNum);
		par.ReaderWork.GetBytes(dataBuf);
		this.CreatePacket(3, 5, 1, 281, dataBuf);
	}
	protected void Release0() {
	}
}
