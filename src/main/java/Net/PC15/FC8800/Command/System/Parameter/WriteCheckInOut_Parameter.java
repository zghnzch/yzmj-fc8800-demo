package Net.PC15.FC8800.Command.System.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
public class WriteCheckInOut_Parameter extends CommandParameter {
	public short Mode;
	public WriteCheckInOut_Parameter(CommandDetail detail) {
		super(detail);
	}
}
