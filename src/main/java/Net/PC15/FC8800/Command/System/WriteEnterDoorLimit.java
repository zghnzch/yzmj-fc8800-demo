package Net.PC15.FC8800.Command.System;
import Net.PC15.FC8800.Command.Data.DoorLimit;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteEnterDoorLimit_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WriteEnterDoorLimit extends FC8800Command {
	public WriteEnterDoorLimit(WriteEnterDoorLimit_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(36);
		DoorLimit limit = par.Limit;
		dataBuf.writeInt((int) limit.GlobalLimit);
		int i;
		for (i = 0; i < 4; ++i) {
			dataBuf.writeInt((int) limit.DoorLimit[i]);
		}
		for (i = 0; i < 4; ++i) {
			dataBuf.writeInt((int) limit.DoorEnter[i]);
		}
		this.CreatePacket(1, 10, 12, 36, dataBuf);
	}
	protected void Release0() {
	}
}
