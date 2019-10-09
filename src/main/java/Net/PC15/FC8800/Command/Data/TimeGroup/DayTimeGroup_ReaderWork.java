package Net.PC15.FC8800.Command.Data.TimeGroup;
public class DayTimeGroup_ReaderWork extends DayTimeGroup {
	public DayTimeGroup_ReaderWork(int SegmentCount) {
		super(SegmentCount);
	}
	public void SetSegmentCount(int SegmentCount) {
		this.mSegment = new TimeSegment_ReaderWork[SegmentCount];
		for (int i = 0; i < SegmentCount; ++i) {
			this.mSegment[i] = new TimeSegment_ReaderWork();
		}
	}
}
