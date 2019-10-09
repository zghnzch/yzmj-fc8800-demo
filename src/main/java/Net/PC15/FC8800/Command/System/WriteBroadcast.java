package Net.PC15.FC8800.Command.System;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteBroadcast_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WriteBroadcast extends FC8800Command {
	public WriteBroadcast(WriteBroadcast_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(10);
		dataBuf.writeBytes(par.Broadcast.Broadcast);
		this.CreatePacket(1, 10, 8, 10, dataBuf);
	}
	protected void Release0() {
	}
}
