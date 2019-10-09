package Net.PC15.FC8800.Command.Data.TimeGroup;
import Net.PC15.Data.INData;
import Net.PC15.FC8800.Command.Data.E_WeekDay;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WeekTimeGroup implements INData {
	protected DayTimeGroup[] mDay;
	protected int mIndex;
	protected int DaySegmentCount;
	public WeekTimeGroup(int iDaySegmentCount) {
		this.DaySegmentCount = iDaySegmentCount;
		this.CreateDayTimeGroup();
	}
	public WeekTimeGroup(int iDaySegmentCount, int index) {
		this(iDaySegmentCount);
		this.mIndex = index;
	}
	protected void CreateDayTimeGroup() {
		this.mDay = new DayTimeGroup[7];
		for (int i = 0; i < 7; ++i) {
			this.mDay[i] = new DayTimeGroup(this.DaySegmentCount);
		}
	}
	public int GetIndex() {
		return this.mIndex;
	}
	public void SetIndex(int index) {
		this.mIndex = index;
	}
	public DayTimeGroup GetItem(E_WeekDay week) {
		return this.mDay[week.getValue()];
	}
	public int GetDataLen() {
		return 7 * this.DaySegmentCount * 4;
	}
	public void SetBytes(ByteBuf data) {
		data.readByte();
		data.readByte();
		this.SetBytes(E_WeekDay.Monday, data);
	}
	public void SetBytes(E_WeekDay FistWeek, ByteBuf data) {
		int[] WeekList = new int[7];
		this.GetWeekList(FistWeek, WeekList);
		for (int i = 0; i < 7; ++i) {
			this.mDay[WeekList[i]].SetBytes(data);
		}
	}
	protected void GetWeekList(E_WeekDay FistWeek, int[] WeekList) {
		int lBeginIndex = FistWeek.getValue();
		int iEndIndex = 6 - lBeginIndex;
		int i;
		for (i = 0; i <= iEndIndex; ++i) {
			WeekList[i] = lBeginIndex + i;
		}
		if (lBeginIndex != 0) {
			++iEndIndex;
			lBeginIndex = 0;
			for (i = iEndIndex; i <= 6; ++i) {
				WeekList[i] = lBeginIndex++;
			}
		}
	}
	public ByteBuf GetBytes() {
		return null;
	}
	public void GetBytes(ByteBuf data) {
		this.GetBytes(E_WeekDay.Monday, data);
	}
	public void GetBytes(E_WeekDay FistWeek, ByteBuf data) {
		int[] WeekList = new int[7];
		this.GetWeekList(FistWeek, WeekList);
		for (int i = 0; i < 7; ++i) {
			this.mDay[WeekList[i]].GetBytes(data);
		}
	}
	public WeekTimeGroup Clone() {
		WeekTimeGroup w = new WeekTimeGroup(this.DaySegmentCount);
		ByteBuf bBuf = ByteUtil.ALLOCATOR.buffer(this.DaySegmentCount * 4);
		for (int i = 0; i < 10; ++i) {
			this.mDay[i].GetBytes(bBuf);
			w.mDay[i].SetBytes(bBuf);
			bBuf.clear();
		}
		return w;
	}
}
