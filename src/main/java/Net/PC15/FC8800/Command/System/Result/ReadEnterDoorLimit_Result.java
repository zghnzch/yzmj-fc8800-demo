package Net.PC15.FC8800.Command.System.Result;
import Net.PC15.Command.INCommandResult;
import Net.PC15.FC8800.Command.Data.DoorLimit;
public class ReadEnterDoorLimit_Result implements INCommandResult {
	public DoorLimit Limit;
	public void release() {
	}
}
