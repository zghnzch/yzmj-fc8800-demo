package Net.PC15.FC8800.Command.Data;
public class BroadcastDetail {
	public final byte[] Broadcast;
	public BroadcastDetail() {
		this.Broadcast = new byte[10];
	}
	public BroadcastDetail(byte[] data) {
		if (data.length != 10) {
			throw new IllegalArgumentException("data.length != 10");
		}
		else {
			this.Broadcast = data;
		}
	}
	public boolean GetValue(int iIndex) {
		if (iIndex > 0 && iIndex <= 80) {
			--iIndex;
			int iByteIndex = iIndex / 8;
			int iBitIndex = iIndex % 8;
			int iByteValue = this.Broadcast[iByteIndex] & 255;
			int iMaskValue = (int) Math.pow(2.0D, iBitIndex);
			iByteValue &= iMaskValue;
			if (iBitIndex > 0) {
				iByteValue >>= iBitIndex;
			}
			return iByteValue == 1;
		}
		else {
			throw new IllegalArgumentException("iIndex= 1 -- 80");
		}
	}
	public void SetValue(int iIndex, boolean bUse) {
		if (iIndex > 0 && iIndex <= 80) {
			if (bUse != this.GetValue(iIndex)) {
				--iIndex;
				int iByteIndex = iIndex / 8;
				int iBitIndex = iIndex % 8;
				int iByteValue = this.Broadcast[iByteIndex] & 255;
				int iMaskValue = (int) Math.pow(2.0D, iBitIndex);
				if (bUse) {
					iByteValue |= iMaskValue;
				}
				else {
					iByteValue ^= iMaskValue;
				}
				this.Broadcast[iByteIndex] = (byte) iByteValue;
			}
		}
		else {
			throw new IllegalArgumentException("iIndex= 1 -- 80");
		}
	}
}
