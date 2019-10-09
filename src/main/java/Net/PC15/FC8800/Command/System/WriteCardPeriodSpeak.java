package Net.PC15.FC8800.Command.System;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteCardPeriodSpeak_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WriteCardPeriodSpeak extends FC8800Command {
	public WriteCardPeriodSpeak(WriteCardPeriodSpeak_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
		int iValue = 0;
		if (par.Use) {
			iValue = 1;
		}
		dataBuf.writeByte(iValue);
		this.CreatePacket(1, 10, 16, 1, dataBuf);
	}
	protected void Release0() {
	}
}
