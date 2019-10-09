package Net.PC15.FC8800.Command.Card.Result;
import Net.PC15.Command.INCommandResult;
import Net.PC15.FC8800.Command.Data.CardDetail;
public class ReadCardDetail_Result implements INCommandResult {
	public boolean IsReady;
	public CardDetail Card;
	public void release() {
		this.Card = null;
	}
}
