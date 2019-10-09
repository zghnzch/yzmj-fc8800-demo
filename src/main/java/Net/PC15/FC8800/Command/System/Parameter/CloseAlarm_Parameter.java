package Net.PC15.FC8800.Command.System.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.Data.CloseAlarmType;
public class CloseAlarm_Parameter extends CommandParameter {
	public int Door = 255;
	public CloseAlarmType Alarm = new CloseAlarmType();
	public CloseAlarm_Parameter(CommandDetail detail) {
		super(detail);
	}
}
