package Net.PC15.FC8800.Command.Card.Result;
import Net.PC15.Command.INCommandResult;

import java.util.ArrayList;
public class ReadCardDataBase_Result implements INCommandResult {
	public ArrayList CardList = null;
	public int DataBaseSize = 0;
	public int CardType;
	public ReadCardDataBase_Result(int type) {
		this.CardType = type;
	}
	public void release() {
		this.CardList.clear();
		this.CardList = null;
	}
}
