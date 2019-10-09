package Net.PC15.FC8800.Command.Data.TimeGroup;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WeekTimeGroup_ReaderWork extends WeekTimeGroup {
	public WeekTimeGroup_ReaderWork(int iDaySegmentCount) {
		super(iDaySegmentCount);
	}
	public int GetDataLen() {
		return 7 * this.DaySegmentCount * 5;
	}
	protected void CreateDayTimeGroup() {
		this.mDay = new DayTimeGroup_ReaderWork[7];
		for (int i = 0; i < 7; ++i) {
			this.mDay[i] = new DayTimeGroup_ReaderWork(this.DaySegmentCount);
		}
	}
	public WeekTimeGroup Clone() {
		WeekTimeGroup_ReaderWork w = new WeekTimeGroup_ReaderWork(this.DaySegmentCount);
		ByteBuf bBuf = ByteUtil.ALLOCATOR.buffer(this.DaySegmentCount * 5);
		for (int i = 0; i < 10; ++i) {
			this.mDay[i].GetBytes(bBuf);
			w.mDay[i].SetBytes(bBuf);
			bBuf.clear();
		}
		return w;
	}
}
