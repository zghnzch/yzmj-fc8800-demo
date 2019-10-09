package Net.PC15.FC8800.Command.Card.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
public class DeleteCard_Parameter extends CommandParameter {
	public String[] CardList;
	public DeleteCard_Parameter(CommandDetail detail, String[] list) {
		super(detail);
		this.CardList = list;
	}
}
