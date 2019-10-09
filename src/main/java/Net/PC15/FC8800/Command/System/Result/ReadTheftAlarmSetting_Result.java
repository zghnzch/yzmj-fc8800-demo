package Net.PC15.FC8800.Command.System.Result;
import Net.PC15.Command.INCommandResult;
import Net.PC15.FC8800.Command.Data.TheftAlarmSetting;
public class ReadTheftAlarmSetting_Result implements INCommandResult {
	public TheftAlarmSetting Setting;
	public void release() {
	}
}
