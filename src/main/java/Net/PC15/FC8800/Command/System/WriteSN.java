package Net.PC15.FC8800.Command.System;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteSN_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WriteSN extends FC8800Command {
	private static final byte[] DataStrt = new byte[]{3, -59, -119, 18, 62};
	private static final byte[] DataEnd = new byte[]{-112, 127, 120};
	public WriteSN(WriteSN_Parameter par) {
		this._Parameter = par;
		String SN = par.GetSN();
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(18);
		dataBuf.writeBytes(DataStrt);
		dataBuf.writeBytes(SN.getBytes());
		dataBuf.writeBytes(DataEnd);
		this.CreatePacket(1, 1, 0, 18, dataBuf);
	}
	protected void Release0() {
	}
}
