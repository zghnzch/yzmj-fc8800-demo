package Net.PC15.FC8800.Command.System;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteLockInteraction_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WriteLockInteraction extends FC8800Command {
	public WriteLockInteraction(WriteLockInteraction_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(4);
		dataBuf.writeBytes(par.DoorPort.DoorPort);
		this.CreatePacket(1, 10, 4, 4, dataBuf);
	}
	protected void Release0() {
	}
}
