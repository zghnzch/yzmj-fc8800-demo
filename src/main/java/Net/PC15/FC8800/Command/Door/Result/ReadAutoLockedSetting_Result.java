package Net.PC15.FC8800.Command.Door.Result;
import Net.PC15.Command.INCommandResult;
import Net.PC15.FC8800.Command.Data.TimeGroup.WeekTimeGroup;
public class ReadAutoLockedSetting_Result implements INCommandResult {
	public int DoorNum;
	public boolean Use;
	public WeekTimeGroup timeGroup = new WeekTimeGroup(8);
	public void release() {
	}
}
