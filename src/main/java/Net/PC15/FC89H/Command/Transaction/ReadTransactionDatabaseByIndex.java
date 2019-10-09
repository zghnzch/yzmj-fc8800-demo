package Net.PC15.FC89H.Command.Transaction;
import Net.PC15.Data.AbstractTransaction;
import Net.PC15.FC8800.Command.Data.*;
import Net.PC15.FC8800.Command.Transaction.Parameter.ReadTransactionDatabaseByIndex_Parameter;
import Net.PC15.FC8800.Command.Transaction.Result.ReadTransactionDatabaseByIndex_Result;
import Net.PC15.FC89H.Command.Data.CardTransaction;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
public class ReadTransactionDatabaseByIndex extends Net.PC15.FC8800.Command.Transaction.ReadTransactionDatabaseByIndex {
	public ReadTransactionDatabaseByIndex(ReadTransactionDatabaseByIndex_Parameter par) {
		super(par);
	}
	protected void Analysis(int iSize) throws Exception {
		ReadTransactionDatabaseByIndex_Result result = (ReadTransactionDatabaseByIndex_Result) this._Result;
		result.Quantity = iSize;
		ArrayList trList = new ArrayList(iSize);
		result.TransactionList = trList;
		Class TransactionType;
		switch (result.DatabaseType) {
			case OnCardTransaction:
				TransactionType = CardTransaction.class;
				break;
			case OnButtonTransaction:
				TransactionType = ButtonTransaction.class;
				break;
			case OnDoorSensorTransaction:
				TransactionType = DoorSensorTransaction.class;
				break;
			case OnSoftwareTransaction:
				TransactionType = SoftwareTransaction.class;
				break;
			case OnAlarmTransaction:
				TransactionType = AlarmTransaction.class;
				break;
			case OnSystemTransaction:
				TransactionType = SystemTransaction.class;
				break;
			default:
				result.Quantity = 0;
				return;
		}
		while (this.mBufs.peek() != null) {
			ByteBuf buf = (ByteBuf) this.mBufs.poll();
			iSize = buf.readInt();
			for (int i = 0; i < iSize; ++i) {
				try {
					AbstractTransaction cd = (AbstractTransaction) TransactionType.newInstance();
					cd.SerialNumber = buf.readInt();
					cd.SetBytes(buf);
					trList.add(cd);
				}
				catch (Exception var8) {
					result.Quantity = 0;
					return;
				}
			}
			buf.release();
		}
	}
}
