package Net.PC15.FC8800.Command.System;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteKeyboard_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WriteKeyboard extends FC8800Command {
	public WriteKeyboard(WriteKeyboard_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
		dataBuf.writeByte(par.Keyboard);
		this.CreatePacket(1, 10, 2, 1, dataBuf);
	}
	protected void Release0() {
	}
}
