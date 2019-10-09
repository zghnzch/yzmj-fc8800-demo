package Net.PC15.FC8800.Command.Data;
import Net.PC15.Data.INData;
import io.netty.buffer.ByteBuf;
public class TransactionDatabaseDetail implements INData {
	public TransactionDetail CardTransactionDetail = new TransactionDetail();
	public TransactionDetail ButtonTransactionDetail = new TransactionDetail();
	public TransactionDetail DoorSensorTransactionDetail = new TransactionDetail();
	public TransactionDetail SoftwareTransactionDetail = new TransactionDetail();
	public TransactionDetail AlarmTransactionDetail = new TransactionDetail();
	public TransactionDetail SystemTransactionDetail = new TransactionDetail();
	public int GetDataLen() {
		return 78;
	}
	public void SetBytes(ByteBuf data) {
		TransactionDetail[] dlst = new TransactionDetail[]{this.CardTransactionDetail, this.ButtonTransactionDetail, this.DoorSensorTransactionDetail, this.SoftwareTransactionDetail, this.AlarmTransactionDetail, this.SystemTransactionDetail};
		for (int i = 0; i < dlst.length; ++i) {
			dlst[i].SetBytes(data);
		}
	}
	public ByteBuf GetBytes() {
		return null;
	}
}
