package Net.PC15.FC8800.Command.System;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteBuzzer_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WriteBuzzer extends FC8800Command {
	public WriteBuzzer(WriteBuzzer_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
		dataBuf.writeByte(par.Buzzer);
		this.CreatePacket(1, 10, 10, 1, dataBuf);
	}
	protected void Release0() {
	}
}
