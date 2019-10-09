package Net.PC15.FC8800.Command.Data;
public class CloseAlarmType {
	public int Alarm;
	public boolean GetValue(int iIndex) {
		if (iIndex >= 0 && iIndex <= 12) {
			int iMaskValue = (int) Math.pow(2.0D, iIndex);
			int iByteValue = this.Alarm & iMaskValue;
			if (iIndex > 0) {
				iByteValue >>= iIndex;
			}
			return iByteValue == 1;
		}
		else {
			throw new IllegalArgumentException("iIndex= 0 -- 12");
		}
	}
	public void SetValue(int iIndex, boolean bUse) {
		if (iIndex >= 0 && iIndex <= 12) {
			int iMaskValue = (int) Math.pow(2.0D, iIndex);
			if (bUse) {
				this.Alarm |= iMaskValue;
			}
			else {
				this.Alarm ^= iMaskValue;
			}
		}
		else {
			throw new IllegalArgumentException("iIndex= 0 -- 12");
		}
	}
}
