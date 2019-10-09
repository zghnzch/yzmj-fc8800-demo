package Net.PC15.FC8800.Command.System;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteReaderIntervalTime_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WriteReaderIntervalTime extends FC8800Command {
	public WriteReaderIntervalTime(WriteReaderIntervalTime_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(2);
		dataBuf.writeShort(par.IntervalTime);
		this.CreatePacket(1, 10, 7, 2, dataBuf);
	}
	protected void Release0() {
	}
}
