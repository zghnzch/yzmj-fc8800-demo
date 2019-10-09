package Net.PC15.FC8800.Command.System.Result;
import Net.PC15.Command.INCommandResult;
public class ReadDeadline_Result implements INCommandResult {
	public final int Deadline;
	public ReadDeadline_Result(int deadline) {
		this.Deadline = deadline;
	}
	public void release() {
	}
}
