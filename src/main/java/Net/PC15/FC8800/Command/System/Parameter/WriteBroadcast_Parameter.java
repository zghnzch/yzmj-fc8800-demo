package Net.PC15.FC8800.Command.System.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.Data.BroadcastDetail;
public class WriteBroadcast_Parameter extends CommandParameter {
	public BroadcastDetail Broadcast = new BroadcastDetail();
	public WriteBroadcast_Parameter(CommandDetail detail) {
		super(detail);
	}
}
