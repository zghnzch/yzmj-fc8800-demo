package Net.PC15.FC8800.Command.Door.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
public class DoorPort_Parameter extends CommandParameter {
	public int Door;
	public DoorPort_Parameter(CommandDetail detail, int iDoor) {
		super(detail);
	}
}
