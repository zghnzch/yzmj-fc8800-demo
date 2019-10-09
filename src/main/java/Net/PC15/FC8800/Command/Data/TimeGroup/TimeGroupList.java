package Net.PC15.FC8800.Command.Data.TimeGroup;
public class TimeGroupList {
	protected WeekTimeGroup[] mWeekTimeGroup = new WeekTimeGroup[64];
	protected int DaySegmentCount;
	public TimeGroupList(int iDaySegmentCount) {
		for (int i = 0; i < 64; ++i) {
			this.mWeekTimeGroup[i] = new WeekTimeGroup(iDaySegmentCount, i + 1);
		}
	}
	public int Count() {
		return 64;
	}
	public WeekTimeGroup GetItem(int iIndex) {
		return this.mWeekTimeGroup[iIndex];
	}
}
