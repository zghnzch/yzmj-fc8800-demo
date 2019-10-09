package Net.PC15.FC8800.Command.System;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteRecordMode_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WriteRecordMode extends FC8800Command {
	public WriteRecordMode(WriteRecordMode_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
		dataBuf.writeByte(par.Mode);
		this.CreatePacket(1, 10, 1, 1, dataBuf);
	}
	protected void Release0() {
	}
}
