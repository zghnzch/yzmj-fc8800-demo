package Net.PC15.FC8800.Command.System;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteDeadline_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WriteDeadline extends FC8800Command {
	public WriteDeadline(WriteDeadline_Parameter par) {
		this._Parameter = par;
		int Deadline = par.GetDeadline();
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(2);
		dataBuf.writeShort(Deadline);
		this.CreatePacket(1, 7, 1, 2, dataBuf);
	}
	protected void Release0() {
	}
}
