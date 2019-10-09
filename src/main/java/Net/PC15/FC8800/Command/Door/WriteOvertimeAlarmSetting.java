package Net.PC15.FC8800.Command.Door;
import Net.PC15.FC8800.Command.Door.Parameter.WriteOvertimeAlarmSetting_Parameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WriteOvertimeAlarmSetting extends FC8800Command {
	public WriteOvertimeAlarmSetting(WriteOvertimeAlarmSetting_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(5);
		dataBuf.writeByte(par.DoorNum);
		dataBuf.writeBoolean(par.Use);
		dataBuf.writeShort(par.Overtime);
		dataBuf.writeBoolean(par.Alarm);
		this.CreatePacket(3, 14, 1, 5, dataBuf);
	}
	protected void Release0() {
	}
}
