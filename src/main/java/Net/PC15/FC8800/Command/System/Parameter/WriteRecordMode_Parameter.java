package Net.PC15.FC8800.Command.System.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
public class WriteRecordMode_Parameter extends CommandParameter {
	public short Mode;
	public WriteRecordMode_Parameter(CommandDetail detail) {
		super(detail);
	}
}
