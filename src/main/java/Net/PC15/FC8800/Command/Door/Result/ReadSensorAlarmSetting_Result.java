package Net.PC15.FC8800.Command.Door.Result;
import Net.PC15.Command.INCommandResult;
import Net.PC15.FC8800.Command.Data.TimeGroup.WeekTimeGroup;
public class ReadSensorAlarmSetting_Result implements INCommandResult {
	public int DoorNum;
	public boolean Use;
	public WeekTimeGroup TimeGroup = new WeekTimeGroup(8);
	public void release() {
	}
}
