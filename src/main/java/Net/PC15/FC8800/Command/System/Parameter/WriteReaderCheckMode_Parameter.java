package Net.PC15.FC8800.Command.System.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
public class WriteReaderCheckMode_Parameter extends CommandParameter {
	public short ReaderCheckMode;
	public WriteReaderCheckMode_Parameter(CommandDetail detail) {
		super(detail);
	}
}
