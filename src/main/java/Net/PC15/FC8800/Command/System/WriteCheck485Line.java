package Net.PC15.FC8800.Command.System;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteCheck485Line_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WriteCheck485Line extends FC8800Command {
	public WriteCheck485Line(WriteCheck485Line_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
		int v = 0;
		if (par.Use) {
			v = 1;
		}
		dataBuf.writeByte(v);
		this.CreatePacket(1, 19, 0, 1, dataBuf);
	}
	protected void Release0() {
	}
}
