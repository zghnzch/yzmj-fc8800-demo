package Net.PC15.FC8800.Command.Data.TimeGroup;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class TimeSegment_ReaderWork extends TimeSegment {
	protected int mWortType = 0;
	public boolean GetWorkType(ReaderWorkType type) {
		int iBitIndex = type.getValue() % 8;
		int iMaskValue = (int) Math.pow(2.0D, iBitIndex);
		int iByteValue = this.mWortType & iMaskValue;
		if (iBitIndex > 0) {
			iByteValue >>= iBitIndex;
		}
		return iByteValue == 1;
	}
	public void SetWorkType(ReaderWorkType type, boolean bUse) {
		if (bUse != this.GetWorkType(type)) {
			int iBitIndex = type.getValue() % 8;
			int iMaskValue = (int) Math.pow(2.0D, iBitIndex);
			if (bUse) {
				this.mWortType |= iMaskValue;
			}
			else {
				this.mWortType ^= iMaskValue;
			}
		}
	}
	public void GetBytes(ByteBuf bBuf) {
		super.GetBytes(bBuf);
		bBuf.writeByte(ByteUtil.ByteToBCD((byte) this.mWortType));
	}
	public void SetBytes(ByteBuf bBuf) {
		super.SetBytes(bBuf);
		this.mWortType = bBuf.readByte();
	}
	public enum ReaderWorkType {
		OnlyCard(0), OnlyPassword(1), CardAndPassword(2), InputCardAndPassword(3);
		private final int value;
		ReaderWorkType(int value) {
			this.value = value;
		}
		public int getValue() {
			return this.value;
		}
	}
}
