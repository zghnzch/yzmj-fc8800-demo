package Net.PC15.FC8800.Command.System;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteTheftAlarmSetting_Parameter;
import io.netty.buffer.ByteBuf;
public class WriteTheftAlarmSetting extends FC8800Command {
	public WriteTheftAlarmSetting(WriteTheftAlarmSetting_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = par.Setting.GetBytes();
		this.CreatePacket(1, 10, 14, par.Setting.GetDataLen(), dataBuf);
	}
	protected void Release0() {
	}
}
