package Net.PC15.FC8800.Command.Door.Result;
import Net.PC15.Command.INCommandResult;
public class ReadAlarmPassword_Result implements INCommandResult {
	public int DoorNum;
	public boolean Use;
	public String Password;
	public int AlarmOption;
	public void release() {
	}
}
