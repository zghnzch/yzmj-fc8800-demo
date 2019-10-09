package Net.PC15.FC8800.Command.Door.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
public class WriteOvertimeAlarmSetting_Parameter extends CommandParameter {
	public int DoorNum;
	public boolean Use;
	public int Overtime;
	public boolean Alarm;
	public WriteOvertimeAlarmSetting_Parameter(CommandDetail detail, int door, boolean use, int overtime, boolean alarm) {
		super(detail);
		this.DoorNum = door;
		this.Use = use;
		this.Overtime = overtime;
		this.Alarm = alarm;
	}
}
