package Net.PC15.FC8800.Command.Door.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.Data.TimeGroup.WeekTimeGroup_ReaderWork;
public class WriteReaderWorkSetting_Parameter extends CommandParameter {
	public WeekTimeGroup_ReaderWork ReaderWork = new WeekTimeGroup_ReaderWork(8);
	public int DoorNum;
	public WriteReaderWorkSetting_Parameter(CommandDetail detail, int door) {
		super(detail);
		this.DoorNum = door;
	}
}
