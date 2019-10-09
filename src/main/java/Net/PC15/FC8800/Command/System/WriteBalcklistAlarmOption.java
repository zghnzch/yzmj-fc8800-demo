package Net.PC15.FC8800.Command.System;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteBalcklistAlarmOption_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WriteBalcklistAlarmOption extends FC8800Command {
	public WriteBalcklistAlarmOption(WriteBalcklistAlarmOption_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
		int v = 0;
		if (par.Use) {
			v = 1;
		}
		dataBuf.writeByte(v);
		this.CreatePacket(1, 18, 0, 1, dataBuf);
	}
	protected void Release0() {
	}
}
