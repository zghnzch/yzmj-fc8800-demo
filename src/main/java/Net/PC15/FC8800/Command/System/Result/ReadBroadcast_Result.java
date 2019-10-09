package Net.PC15.FC8800.Command.System.Result;
import Net.PC15.Command.INCommandResult;
import Net.PC15.FC8800.Command.Data.BroadcastDetail;
public class ReadBroadcast_Result implements INCommandResult {
	public BroadcastDetail Broadcast;
	public void release() {
	}
}
