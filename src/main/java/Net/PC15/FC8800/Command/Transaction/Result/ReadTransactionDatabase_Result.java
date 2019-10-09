package Net.PC15.FC8800.Command.Transaction.Result;
import Net.PC15.Command.INCommandResult;
import Net.PC15.FC8800.Command.Transaction.e_TransactionDatabaseType;

import java.util.ArrayList;
public class ReadTransactionDatabase_Result implements INCommandResult {
	public e_TransactionDatabaseType DatabaseType;
	public int Quantity = 0;
	public int readable = 0;
	public ArrayList TransactionList;
	public void release() {
	}
}
