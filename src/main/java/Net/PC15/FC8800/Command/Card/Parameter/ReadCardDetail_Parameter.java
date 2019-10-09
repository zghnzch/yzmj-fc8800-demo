package Net.PC15.FC8800.Command.Card.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
public class ReadCardDetail_Parameter extends CommandParameter {
	public String CardData;
	public ReadCardDetail_Parameter(CommandDetail detail, String data) {
		super(detail);
		this.CardData = data;
	}
}
