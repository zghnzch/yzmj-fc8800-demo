package Net.PC15.FC8800.Command.DateTime.Result;
import Net.PC15.Command.INCommandResult;

import java.util.Calendar;
public class ReadTime_Result implements INCommandResult {
	public final Calendar ControllerDate;
	public ReadTime_Result(Calendar t) {
		this.ControllerDate = t;
	}
	public void release() {
	}
}
