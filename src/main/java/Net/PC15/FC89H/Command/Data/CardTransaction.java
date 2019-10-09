package Net.PC15.FC89H.Command.Data;
import Net.PC15.Util.ByteUtil;
import Net.PC15.Util.StringUtil;
import Net.PC15.Util.TimeUtil;
import io.netty.buffer.ByteBuf;
public class CardTransaction extends Net.PC15.FC8800.Command.Data.CardTransaction {
	public String CardDataHEX;
	public String CardDataStr;
	public void SetBytes(ByteBuf data) {
		try {
			data.readByte();
			byte[] btCardData = new byte[8];
			data.readBytes(btCardData, 0, 8);
			this.CardDataHEX = ByteUtil.ByteToHex(btCardData);
			this.CardDataStr = StringUtil.LTrim(this.CardDataHEX, '0');
			try {
				this.CardDataStr = StringUtil.HexStr2Str(this.CardDataHEX, 16);
			}
			catch (Exception var4) {
			}
			byte[] btTime = new byte[6];
			data.readBytes(btTime, 0, 6);
			this._TransactionDate = TimeUtil.BCDTimeToDate_yyMMddhhmmss(btTime);
			this.Reader = data.readUnsignedByte();
			this._TransactionCode = data.readUnsignedByte();
			if (this._TransactionCode == 0 || this.Reader == 0 || this.Reader > 8 || this._TransactionDate == null) {
				this._IsNull = true;
			}
		}
		catch (Exception var5) {
		}
	}
}
