package Net.PC15.FC8800.Command.Door.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
public class WriteAntiPassback_Parameter extends CommandParameter {
	public int DoorNum;
	public boolean Use;
	public WriteAntiPassback_Parameter(CommandDetail detail, int door, boolean use) {
		super(detail);
		this.DoorNum = door;
		this.Use = use;
	}
}
