package Net.PC15.FC8800.Command.DateTime.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;

import java.util.Calendar;
public class WriteTimeDefine_Parameter extends CommandParameter {
	public final Calendar ControllerDate;
	public WriteTimeDefine_Parameter(CommandDetail detail, Calendar t) {
		super(detail);
		this.ControllerDate = t;
	}
}
