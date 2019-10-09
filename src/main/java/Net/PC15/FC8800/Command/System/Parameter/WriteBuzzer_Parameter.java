package Net.PC15.FC8800.Command.System.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
public class WriteBuzzer_Parameter extends CommandParameter {
	public short Buzzer;
	public WriteBuzzer_Parameter(CommandDetail detail) {
		super(detail);
	}
}
