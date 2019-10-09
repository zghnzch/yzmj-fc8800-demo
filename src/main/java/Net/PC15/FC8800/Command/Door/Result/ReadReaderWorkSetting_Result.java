package Net.PC15.FC8800.Command.Door.Result;
import Net.PC15.Command.INCommandResult;
import Net.PC15.FC8800.Command.Data.TimeGroup.WeekTimeGroup_ReaderWork;
public class ReadReaderWorkSetting_Result implements INCommandResult {
	public WeekTimeGroup_ReaderWork ReaderWork = new WeekTimeGroup_ReaderWork(8);
	public int DoorNum;
	public void release() {
		this.ReaderWork = null;
	}
}
