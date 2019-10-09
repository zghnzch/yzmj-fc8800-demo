package Net.PC15.FC8800.Command.Door.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.Data.TimeGroup.WeekTimeGroup;
public class WriteSensorAlarmSetting_Parameter extends CommandParameter {
	public int DoorNum;
	public boolean Use;
	public WeekTimeGroup TimeGroup;
	public WriteSensorAlarmSetting_Parameter(CommandDetail detail, int door, boolean use) {
		super(detail);
		this.DoorNum = door;
		this.Use = use;
		this.TimeGroup = new WeekTimeGroup(8);
	}
}
