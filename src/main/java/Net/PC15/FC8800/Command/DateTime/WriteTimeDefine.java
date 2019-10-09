package Net.PC15.FC8800.Command.DateTime;
import Net.PC15.FC8800.Command.DateTime.Parameter.WriteTimeDefine_Parameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Util.ByteUtil;
import Net.PC15.Util.TimeUtil;
import io.netty.buffer.ByteBuf;
public class WriteTimeDefine extends FC8800Command {
	public WriteTimeDefine(WriteTimeDefine_Parameter par) {
		this._Parameter = par;
		byte[] Datebuf = new byte[7];
		TimeUtil.DateToBCD_ssmmhhddMMwwyy(Datebuf, par.ControllerDate);
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(7);
		dataBuf.writeBytes(Datebuf);
		this.CreatePacket(2, 2, 0, 7, dataBuf);
	}
	protected void Release0() {
	}
}
