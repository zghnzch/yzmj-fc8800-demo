package Net.PC15.FC8800.Command.Data;
import Net.PC15.Data.AbstractTransaction;
import Net.PC15.Util.TimeUtil;
import io.netty.buffer.ByteBuf;
public class CardTransaction extends AbstractTransaction {
	public long CardData;
	public short Reader;
	public CardTransaction() {
		this._TransactionType = 1;
	}
	public int GetDataLen() {
		return 13;
	}
	public void SetBytes(ByteBuf data) {
		try {
			if (data.readUnsignedByte() == 255) {
				this._IsNull = true;
			}
			this.CardData = data.readUnsignedInt();
			byte[] btTime = new byte[6];
			data.readBytes(btTime, 0, 6);
			this._TransactionDate = TimeUtil.BCDTimeToDate_yyMMddhhmmss(btTime);
			this.Reader = data.readUnsignedByte();
			this._TransactionCode = data.readUnsignedByte();
			if (this._TransactionCode == 0 || this.Reader == 0 || this.Reader > 8 || this._TransactionDate == null) {
				this._IsNull = true;
			}
		}
		catch (Exception var3) {
		}
	}
	public ByteBuf GetBytes() {
		return null;
	}
	public short DoorNum() {
		switch (this.Reader) {
			case 1:
			case 2:
				return 1;
			case 3:
			case 4:
				return 2;
			case 5:
			case 6:
				return 3;
			case 7:
			case 8:
				return 4;
			default:
				return 0;
		}
	}
	public boolean IsEnter() {
		if (this.Reader != 0 && this.Reader <= 8) {
			return this.Reader % 2 == 1;
		}
		else {
			return false;
		}
	}
	public boolean IsExit() {
		return !this.IsEnter();
	}
}
