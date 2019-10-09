package Net.PC15.FC8800.Command.System;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteFireAlarmOption_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WriteFireAlarmOption extends FC8800Command {
	public WriteFireAlarmOption(WriteFireAlarmOption_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
		dataBuf.writeByte(par.Option);
		this.CreatePacket(1, 10, 5, 1, dataBuf);
	}
	protected void Release0() {
	}
}
