package Net.PC15.FC8800.Command.Door.Result;
import Net.PC15.Command.INCommandResult;
import Net.PC15.FC8800.Command.Data.TimeGroup.WeekTimeGroup;
public class ReadDoorWorkSetting_Result implements INCommandResult {
	public int DoorNum;
	public boolean Use;
	public int DoorWorkType;
	public int HoldDoorOption;
	public WeekTimeGroup timeGroup = new WeekTimeGroup(8);
	public void release() {
	}
}
