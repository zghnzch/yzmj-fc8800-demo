package Net.PC15.FC89H.Command.Transaction;
import Net.PC15.Data.AbstractTransaction;
import Net.PC15.FC8800.Command.Data.*;
import Net.PC15.FC8800.Command.Transaction.Parameter.ReadTransactionDatabase_Parameter;
import Net.PC15.FC8800.Command.Transaction.Result.ReadTransactionDatabase_Result;
import Net.PC15.FC89H.Command.Data.CardTransaction;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
public class ReadTransactionDatabase extends Net.PC15.FC8800.Command.Transaction.ReadTransactionDatabase {
	public ReadTransactionDatabase(ReadTransactionDatabase_Parameter par) {
		super(par);
	}
	protected void Analysis(int iSize) throws Exception {
		ReadTransactionDatabase_Result result = (ReadTransactionDatabase_Result) this._Result;
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
		ByteBuf buf;
		for (; this.mBufs.peek() != null; buf.release()) {
			buf = (ByteBuf) this.mBufs.poll();
			iSize = buf.readInt();
			Object o = TransactionType.newInstance();
			if (o instanceof Net.PC15.FC8800.Command.Data.CardTransaction) {
				if ((buf.capacity() - 4) % 21 != 0) {
					buf.release();
					throw new Exception("数据流长度不正确");
				}
				for (int i = 0; i < iSize; ++i) {
					try {
						AbstractTransaction cd = (AbstractTransaction) TransactionType.newInstance();
						cd.SerialNumber = buf.readInt();
						cd.SetBytes(buf);
						trList.add(cd);
					}
					catch (Exception var9) {
						result.Quantity = 0;
						return;
					}
				}
			}
		}
	}
}
