package Net.PC15.FC8800.Command.Door.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.Data.TimeGroup.WeekTimeGroup;
public class WriteAutoLockedSetting_Parameter extends CommandParameter {
	public int DoorNum;
	public boolean Use;
	public WeekTimeGroup timeGroup = new WeekTimeGroup(8);
	public WriteAutoLockedSetting_Parameter(CommandDetail detail) {
		super(detail);
	}
}
