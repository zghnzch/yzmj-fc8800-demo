package Net.PC15.FC8800.Command.Transaction.Result;
import Net.PC15.Command.INCommandResult;
import Net.PC15.FC8800.Command.Data.TransactionDatabaseDetail;
public class ReadTransactionDatabaseDetail_Result implements INCommandResult {
	public TransactionDatabaseDetail DatabaseDetail = new TransactionDatabaseDetail();
	public void release() {
	}
}
