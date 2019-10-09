package Net.PC15.FC8800.Command.Transaction;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.FC8800Command;
public class TransactionDatabaseEmpty extends FC8800Command {
	public TransactionDatabaseEmpty(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(8, 2);
	}
	protected void Release0() {
	}
}
