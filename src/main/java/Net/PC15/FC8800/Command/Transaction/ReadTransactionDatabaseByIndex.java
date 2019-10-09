package Net.PC15.FC8800.Command.Transaction;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.Data.AbstractTransaction;
import Net.PC15.FC8800.Command.Data.*;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.Transaction.Parameter.ReadTransactionDatabaseByIndex_Parameter;
import Net.PC15.FC8800.Command.Transaction.Result.ReadTransactionDatabaseByIndex_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
public class ReadTransactionDatabaseByIndex extends FC8800Command {
	protected ConcurrentLinkedQueue mBufs;
	private int mQuantity;
	public ReadTransactionDatabaseByIndex(ReadTransactionDatabaseByIndex_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(9);
		dataBuf.writeByte(par.DatabaseType.getValue());
		dataBuf.writeInt(par.ReadIndex);
		dataBuf.writeInt(par.Quantity);
		this.mBufs = new ConcurrentLinkedQueue();
		this.CreatePacket(8, 4, 0, 9, dataBuf);
		this.mQuantity = par.Quantity;
		this._ProcessMax = this.mQuantity;
	}
	protected void Release0() {
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		ByteBuf buf;
		int iSize;
		if (this.CheckResponse_Cmd(model, 8, 4, 0)) {
			try {
				buf = model.GetDatabuff();
				iSize = buf.getInt(0);
				this._ProcessStep += iSize;
				this.RaiseCommandProcessEvent(oEvent);
				buf.retain();
				this.mBufs.add(buf);
				this.CommandWaitResponse();
			}
			catch (Exception var8) {
				System.out.println("发送错误：" + var8.getLocalizedMessage());
			}
			return true;
		}
		else if (this.CheckResponse_Cmd(model, 8, 4, 255, 4)) {
			buf = model.GetDatabuff();
			iSize = buf.readInt();
			ReadTransactionDatabaseByIndex_Result result = new ReadTransactionDatabaseByIndex_Result();
			ReadTransactionDatabaseByIndex_Parameter par = (ReadTransactionDatabaseByIndex_Parameter) this._Parameter;
			result.DatabaseType = par.DatabaseType;
			result.ReadIndex = par.ReadIndex;
			result.Quantity = iSize;
			this._Result = result;
			if (iSize > 0) {
				try {
					this.Analysis(iSize);
				}
				catch (Exception var9) {
				}
				this.RaiseCommandCompleteEvent(oEvent);
			}
			else {
				this.RaiseCommandCompleteEvent(oEvent);
			}
			return true;
		}
		else {
			return false;
		}
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
	@Override
	protected void CommandOver_ReSend() {
		this.ClearBuf();
		this._ProcessMax = this.mQuantity;
		this._ProcessStep = 0;
	}
	protected void ClearBuf() {
		if (this.mBufs != null) {
			while (this.mBufs.peek() != null) {
				ByteBuf buf = (ByteBuf) this.mBufs.poll();
				buf.release();
			}
		}
	}
}
