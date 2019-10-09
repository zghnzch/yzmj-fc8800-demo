package Net.PC15.FC8800.Command.System.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.Data.ReadCardSpeak;
public class WriteReadCardSpeak_Parameter extends CommandParameter {
	public ReadCardSpeak SpeakSetting;
	public WriteReadCardSpeak_Parameter(CommandDetail detail) {
		super(detail);
	}
}
