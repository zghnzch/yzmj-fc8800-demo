package Net.PC15.FC8800.Command.System.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.Data.TheftAlarmSetting;
public class WriteTheftAlarmSetting_Parameter extends CommandParameter {
	public TheftAlarmSetting Setting;
	public WriteTheftAlarmSetting_Parameter(CommandDetail detail) {
		super(detail);
	}
}
