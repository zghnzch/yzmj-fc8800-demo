package Net.PC15.FC8800.Command.System.Result;
import Net.PC15.Command.INCommandResult;
public class ReadTheftAlarmState_Result implements INCommandResult {
	public int TheftState;
	public int TheftAlarm;
	public void release() {
	}
}
