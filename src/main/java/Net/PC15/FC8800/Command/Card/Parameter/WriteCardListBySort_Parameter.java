package Net.PC15.FC8800.Command.Card.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;

import java.util.ArrayList;
import java.util.Collections;
public class WriteCardListBySort_Parameter extends CommandParameter {
	public ArrayList CardList;
	public WriteCardListBySort_Parameter(CommandDetail detail, ArrayList list) {
		super(detail);
		this.CardList = list;
		if (this.CardList != null) {
			Collections.sort(this.CardList);
		}
	}
}
