package Net.PC15.FC8800.Command.Transaction.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.Transaction.e_TransactionDatabaseType;
public class ReadTransactionDatabase_Parameter extends CommandParameter {
	public e_TransactionDatabaseType DatabaseType;
	public int Quantity;
	public int PacketSize;
	public ReadTransactionDatabase_Parameter(CommandDetail detail, e_TransactionDatabaseType type) {
		super(detail);
		this.DatabaseType = type;
		this.PacketSize = 200;
		this.Quantity = 0;
	}
}
