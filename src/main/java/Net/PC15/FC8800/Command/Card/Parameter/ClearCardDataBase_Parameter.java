package Net.PC15.FC8800.Command.Card.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
public class ClearCardDataBase_Parameter extends CommandParameter {
	public int CardType;
	public ClearCardDataBase_Parameter(CommandDetail detail, int type) {
		super(detail);
		this.CardType = type;
	}
}
