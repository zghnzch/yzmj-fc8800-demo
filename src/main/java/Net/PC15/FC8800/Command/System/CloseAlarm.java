package Net.PC15.FC8800.Command.System;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.CloseAlarm_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class CloseAlarm extends FC8800Command {
	public CloseAlarm(CloseAlarm_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(3);
		dataBuf.writeByte(par.Door);
		dataBuf.writeShort(par.Alarm.Alarm);
		this.CreatePacket(1, 13, 0, 3, dataBuf);
	}
	protected void Release0() {
	}
}
