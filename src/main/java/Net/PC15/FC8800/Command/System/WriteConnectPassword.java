package Net.PC15.FC8800.Command.System;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteConnectPassword_Parameter;
import Net.PC15.Util.ByteUtil;
import Net.PC15.Util.StringUtil;
import io.netty.buffer.ByteBuf;
public class WriteConnectPassword extends FC8800Command {
	private static final byte[] NULLPassword = new byte[]{-1, -1, -1, -1};
	public WriteConnectPassword(WriteConnectPassword_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(4);
		String pwd = par.GetPassword();
		if (StringUtil.IsNullOrEmpty(pwd)) {
			dataBuf.writeBytes(NULLPassword);
		}
		else {
			dataBuf.writeBytes(StringUtil.HexToByte(pwd));
		}
		this.CreatePacket(1, 3, 0, 4, dataBuf);
	}
	protected void Release0() {
	}
}
