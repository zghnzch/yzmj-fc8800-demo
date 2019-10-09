package Net.PC15.FC8800.Command.Door;
import Net.PC15.FC8800.Command.Door.Parameter.WriteAlarmPassword_Parameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Util.ByteUtil;
import Net.PC15.Util.StringUtil;
import io.netty.buffer.ByteBuf;
public class WriteAlarmPassword extends FC8800Command {
	public WriteAlarmPassword(WriteAlarmPassword_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(7);
		dataBuf.writeByte(par.DoorNum);
		dataBuf.writeBoolean(par.Use);
		String pwd = par.GetPassword();
		if (StringUtil.IsNullOrEmpty(pwd)) {
			pwd = NULLPassword;
		}
		else {
			pwd = StringUtil.FillString(pwd, 8, "F");
		}
		StringUtil.HextoByteBuf(pwd, dataBuf);
		dataBuf.writeByte(par.AlarmOption);
		this.CreatePacket(3, 11, 0, 7, dataBuf);
	}
	protected void Release0() {
	}
}
