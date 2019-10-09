package Net.PC15.FC8800.Command.System.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
public class WriteKeyboard_Parameter extends CommandParameter {
	public short Keyboard;
	public WriteKeyboard_Parameter(CommandDetail detail) {
		super(detail);
	}
}
