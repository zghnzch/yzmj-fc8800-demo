package Net.PC15.FC8800.Command.Card.Result;
import Net.PC15.Command.INCommandResult;

import java.util.ArrayList;
public class WriteCardListBySort_Result implements INCommandResult {
	public int FailTotal;
	public ArrayList CardList;
	public void release() {
	}
}
