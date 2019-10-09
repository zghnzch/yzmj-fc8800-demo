package Net.PC15.FC8800.Command.System;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteCardDeadlineTipDay_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WriteCardDeadlineTipDay extends FC8800Command {
	public WriteCardDeadlineTipDay(WriteCardDeadlineTipDay_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
		dataBuf.writeByte(par.Day);
		this.CreatePacket(1, 21, 0, 1, dataBuf);
	}
	protected void Release0() {
	}
}
