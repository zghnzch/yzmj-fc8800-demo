package Net.PC15.FC8800.Command.Data;
import Net.PC15.Data.AbstractTransaction;
import io.netty.buffer.ByteBuf;

import java.util.Calendar;
public class DefinedTransaction extends AbstractTransaction {
	public int DataLen;
	public byte[] DataByteBuf;
	public DefinedTransaction(int iType, int iCode, Calendar tDate) {
		this._TransactionType = (short) iType;
		this._TransactionCode = (short) iCode;
		this._TransactionDate = (Calendar) tDate.clone();
		this.DataLen = 0;
	}
	public void SetWatchData(ByteBuf data) {
		if (data == null) {
			this.DataLen = 0;
		}
		else {
			this.DataLen = data.readableBytes();
			if (this.DataLen > 0) {
				this.DataByteBuf = new byte[this.DataLen];
				data.readBytes(this.DataByteBuf);
			}
		}
	}
	public int GetDataLen() {
		return 0;
	}
	public void SetBytes(ByteBuf data) {
	}
	public ByteBuf GetBytes() {
		return null;
	}
}
