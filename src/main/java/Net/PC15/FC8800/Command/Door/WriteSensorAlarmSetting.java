package Net.PC15.FC8800.Command.Door;
import Net.PC15.FC8800.Command.Door.Parameter.WriteSensorAlarmSetting_Parameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WriteSensorAlarmSetting extends FC8800Command {
	public WriteSensorAlarmSetting(WriteSensorAlarmSetting_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(226);
		dataBuf.writeByte(par.DoorNum);
		dataBuf.writeBoolean(par.Use);
		par.TimeGroup.GetBytes(dataBuf);
		this.CreatePacket(3, 16, 1, 226, dataBuf);
	}
	protected void Release0() {
	}
}
