package Net.PC15.FC8800.Command.Data.TimeGroup;
import io.netty.buffer.ByteBuf;
public class DayTimeGroup {
	protected TimeSegment[] mSegment;
	public DayTimeGroup(int SegmentCount) {
		this.SetSegmentCount(SegmentCount);
	}
	public void SetSegmentCount(int SegmentCount) {
		this.mSegment = new TimeSegment[SegmentCount];
		for (int i = 0; i < SegmentCount; ++i) {
			this.mSegment[i] = new TimeSegment();
		}
	}
	public int GetSegmentCount() {
		return this.mSegment == null ? 0 : this.mSegment.length;
	}
	public TimeSegment GetItem(int iIndex) {
		if (iIndex >= 0 && iIndex <= this.GetSegmentCount()) {
			return this.mSegment[iIndex];
		}
		else {
			throw new IllegalArgumentException("iIndex<0 || iIndex > GetSegmentCount()");
		}
	}
	public void GetBytes(ByteBuf bBuf) {
		int iCount = this.GetSegmentCount();
		for (int i = 0; i < iCount; ++i) {
			this.mSegment[i].GetBytes(bBuf);
		}
	}
	public void SetBytes(ByteBuf bBuf) {
		int iCount = this.GetSegmentCount();
		for (int i = 0; i < iCount; ++i) {
			this.mSegment[i].SetBytes(bBuf);
			if (bBuf.readableBytes() == 0) {
				return;
			}
		}
	}
}
