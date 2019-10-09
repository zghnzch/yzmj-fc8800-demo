package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class ResetConnectPassword extends FC8800Command {
	private static final byte[] DataStrt = new byte[]{70, 67, 97, 114, 100, 89, 122};
	public ResetConnectPassword(CommandParameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(7);
		dataBuf.writeBytes(DataStrt);
		this.CreatePacket(1, 5, 0, 7, dataBuf);
	}
	protected void Release0() {
	}
}
