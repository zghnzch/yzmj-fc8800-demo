package Net.PC15.FC8800.Command.System.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
public class WriteKeepAliveInterval_Parameter extends CommandParameter {
	public int IntervalTime;
	public WriteKeepAliveInterval_Parameter(CommandDetail detail) {
		super(detail);
	}
}
