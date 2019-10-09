package Net.PC15.FC8800.Command.Transaction;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.Data.AbstractTransaction;
import Net.PC15.FC8800.Command.Data.*;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.Transaction.Parameter.ReadTransactionDatabase_Parameter;
import Net.PC15.FC8800.Command.Transaction.Result.ReadTransactionDatabase_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
public class ReadTransactionDatabase extends FC8800Command {
	protected ConcurrentLinkedQueue mBufs;
	protected int mReadQuantity;
	protected int mStep;
	protected ReadTransactionDatabase_Parameter thisParameter;
	protected TransactionDetail transactionDetail;
	protected int mReadable;
	protected int mReadTotal;
	public ReadTransactionDatabase(ReadTransactionDatabase_Parameter par) {
		this._Parameter = par;
		this.thisParameter = par;
		this.mStep = 1;
		this.mReadQuantity = this.thisParameter.PacketSize;
		if (this.mReadQuantity == 0 || this.mReadQuantity > 300 || this.mReadQuantity < 0) {
			this.mReadQuantity = 200;
		}
		this.thisParameter.PacketSize = this.mReadQuantity;
		ReadTransactionDatabase_Result result = new ReadTransactionDatabase_Result();
		result.DatabaseType = this.thisParameter.DatabaseType;
		this._Result = result;
		this.CreatePacket(8, 1);
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		switch (this.mStep) {
			case 1:
				return this.CheckDataBaseDetail(oEvent, model);
			case 2:
				return this.CheckReadTransactionResponse(oEvent, model);
			case 3:
				return this.CheckWriteReadIndexResponse(oEvent, model);
			default:
				return false;
		}
	}
	protected boolean CheckDataBaseDetail(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 8, 1, 0, 78)) {
			ByteBuf buf = model.GetDatabuff();
			TransactionDatabaseDetail DatabaseDetail = new TransactionDatabaseDetail();
			DatabaseDetail.SetBytes(buf);
			switch (this.thisParameter.DatabaseType) {
				case OnCardTransaction:
					this.transactionDetail = DatabaseDetail.CardTransactionDetail;
					break;
				case OnButtonTransaction:
					this.transactionDetail = DatabaseDetail.ButtonTransactionDetail;
					break;
				case OnDoorSensorTransaction:
					this.transactionDetail = DatabaseDetail.DoorSensorTransactionDetail;
					break;
				case OnSoftwareTransaction:
					this.transactionDetail = DatabaseDetail.SoftwareTransactionDetail;
					break;
				case OnAlarmTransaction:
					this.transactionDetail = DatabaseDetail.AlarmTransactionDetail;
					break;
				case OnSystemTransaction:
					this.transactionDetail = DatabaseDetail.SystemTransactionDetail;
			}
			if (this.transactionDetail.readable() == 0L) {
				this.RaiseCommandCompleteEvent(oEvent);
				return true;
			}
			else {
				this.mStep = 2;
				this.mBufs = new ConcurrentLinkedQueue();
				this.mReadable = (int) this.transactionDetail.readable();
				if (this.thisParameter.Quantity > 0 && this.thisParameter.Quantity < this.mReadable) {
					this.mReadable = this.thisParameter.Quantity;
				}
				this.mReadQuantity = 0;
				this.mReadTotal = 0;
				this._ProcessMax = this.mReadable;
				this._ProcessStep = 0;
				if (this.transactionDetail.IsCircle) {
					this.transactionDetail.ReadIndex = this.transactionDetail.WriteIndex;
				}
				this.ReadTransactionNext();
				return true;
			}
		}
		else {
			return false;
		}
	}
	private void ReadTransactionNext() {
		this._ProcessStep = this.mReadTotal;
		this.mReadable -= this.mReadQuantity;
		if (this.mReadable <= 0) {
			this.WriteTransactionReadIndex();
		}
		else {
			this.mReadQuantity = this.thisParameter.PacketSize;
			if (this.transactionDetail.ReadIndex == this.transactionDetail.DataBaseMaxSize) {
				this.transactionDetail.ReadIndex = 0L;
			}
			if (this.mReadQuantity > this.mReadable) {
				this.mReadQuantity = this.mReadable;
			}
			int iBeginIndex = (int) this.transactionDetail.ReadIndex + 1;
			int iEndIndex = iBeginIndex + this.mReadQuantity - 1;
			if ((long) iEndIndex > this.transactionDetail.DataBaseMaxSize) {
				this.mReadQuantity = (int) (this.transactionDetail.DataBaseMaxSize - this.transactionDetail.ReadIndex);
				iEndIndex = iBeginIndex + this.mReadQuantity - 1;
			}
			this.transactionDetail.ReadIndex = iEndIndex;
			ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(9);
			dataBuf.writeByte(this.thisParameter.DatabaseType.getValue());
			dataBuf.writeInt(iBeginIndex);
			dataBuf.writeInt(this.mReadQuantity);
			this.CreatePacket(8, 4, 0, 9, dataBuf);
			this.CommandReady();
		}
	}
	protected boolean CheckReadTransactionResponse(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 8, 4, 0)) {
			try {
				ByteBuf buf = model.GetDatabuff();
				int iSize = buf.getInt(0);
				this.mReadTotal += iSize;
				this._ProcessStep = this.mReadTotal;
				this.RaiseCommandProcessEvent(oEvent);
				buf.retain();
				this.mBufs.add(buf);
				this.CommandWaitResponse();
			}
			catch (Exception var5) {
				System.out.println("CheckReadTransactionResponse -- 发生错误：" + var5.getLocalizedMessage());
			}
			return true;
		}
		else if (this.CheckResponse_Cmd(model, 8, 4, 255, 4)) {
			this.ReadTransactionNext();
			return true;
		}
		else {
			return false;
		}
	}
	private void WriteTransactionReadIndex() {
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(6);
		dataBuf.writeByte(this.thisParameter.DatabaseType.getValue());
		dataBuf.writeInt((int) this.transactionDetail.ReadIndex);
		dataBuf.writeBoolean(false);
		this.CreatePacket(8, 3, 0, 6, dataBuf);
		this.mStep = 3;
		this.CommandReady();
	}
	protected boolean CheckWriteReadIndexResponse(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponseOK(model)) {
			ReadTransactionDatabase_Result result = (ReadTransactionDatabase_Result) this._Result;
			result.Quantity = this.mReadTotal;
			result.readable = (int) this.transactionDetail.readable();
			this.CommandOver();
			try {
				this.Analysis(this.mReadTotal);
			}
			catch (Exception var5) {
			}
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
	protected void Release0() {
		this._Parameter = null;
		this._Result = null;
		this.thisParameter = null;
		this.ClearBuf();
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
	protected void ClearBuf() {
		if (this.mBufs != null) {
			while (this.mBufs.peek() != null) {
				ByteBuf buf = (ByteBuf) this.mBufs.poll();
				buf.release();
			}
		}
	}
}
