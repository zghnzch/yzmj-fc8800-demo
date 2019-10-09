package Net.PC15.FC8800.Command.System.Result;
import Net.PC15.Command.INCommandResult;

import java.util.Calendar;
public class ReadSystemStatus_Result implements INCommandResult {
	public int RunDay;
	public int FormatCount;
	public int RestartCount;
	public int UPS;
	public float Temperature;
	public Calendar StartTime;
	public float Voltage;
	public void release() {
	}
}
