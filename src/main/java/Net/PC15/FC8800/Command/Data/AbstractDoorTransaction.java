package Net.PC15.FC8800.Command.Data;
import Net.PC15.Data.AbstractTransaction;
import Net.PC15.Util.ByteUtil;
import Net.PC15.Util.TimeUtil;
import io.netty.buffer.ByteBuf;
public abstract class AbstractDoorTransaction extends AbstractTransaction {
	public short Door;
	public AbstractDoorTransaction(int Type) {
		this._TransactionType = (short) Type;
	}
	public int GetDataLen() {
		return 8;
	}
	public void SetBytes(ByteBuf data) {
		try {
			this.Door = data.readUnsignedByte();
			byte[] btTime = new byte[6];
			data.readBytes(btTime, 0, 6);
			if (ByteUtil.uByte(btTime[0]) == 255) {
				this._IsNull = true;
			}
			this._TransactionDate = TimeUtil.BCDTimeToDate_yyMMddhhmmss(btTime);
			this._TransactionCode = data.readUnsignedByte();
			if (this._TransactionCode == 0) {
				this._IsNull = true;
			}
		}
		catch (Exception var3) {
		}
	}
	public ByteBuf GetBytes() {
		return null;
	}
}
