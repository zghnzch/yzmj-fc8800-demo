package Net.PC15.FC8800.Command.Data;
import Net.PC15.Data.AbstractTransaction;
import Net.PC15.Util.ByteUtil;
import Net.PC15.Util.TimeUtil;
import io.netty.buffer.ByteBuf;
public class SystemTransaction extends AbstractTransaction {
	public SystemTransaction() {
		this._TransactionType = 6;
	}
	public int GetDataLen() {
		return 8;
	}
	public void SetBytes(ByteBuf data) {
		try {
			short code = data.readUnsignedByte();
			if (code == 255) {
				this._IsNull = true;
			}
			this._TransactionCode = code;
			byte[] btTime = new byte[6];
			data.readBytes(btTime, 0, 6);
			if (ByteUtil.uByte(btTime[0]) == 255) {
				this._IsNull = true;
			}
			this._TransactionDate = TimeUtil.BCDTimeToDate_ssmmhhddMMyy(btTime);
			data.readUnsignedByte();
			if (this._TransactionCode == 0) {
				this._IsNull = true;
			}
		}
		catch (Exception var4) {
		}
	}
	public ByteBuf GetBytes() {
		return null;
	}
}
