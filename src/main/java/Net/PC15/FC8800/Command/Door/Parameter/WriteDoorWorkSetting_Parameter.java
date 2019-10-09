package Net.PC15.FC8800.Command.Door.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.Data.TimeGroup.WeekTimeGroup;
public class WriteDoorWorkSetting_Parameter extends CommandParameter {
	public int DoorNum;
	public boolean Use;
	public int DoorWorkType;
	public int HoldDoorOption;
	public WeekTimeGroup timeGroup = new WeekTimeGroup(8);
	public WriteDoorWorkSetting_Parameter(CommandDetail detail) {
		super(detail);
	}
}
