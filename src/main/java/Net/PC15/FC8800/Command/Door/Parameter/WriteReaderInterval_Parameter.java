package Net.PC15.FC8800.Command.Door.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
public class WriteReaderInterval_Parameter extends CommandParameter {
	public int DoorNum;
	public boolean Use;
	public int RecordOption;
	public WriteReaderInterval_Parameter(CommandDetail detail, int door, boolean use, int recordOption) {
		super(detail);
		this.DoorNum = door;
		this.Use = use;
		this.RecordOption = recordOption;
	}
}
