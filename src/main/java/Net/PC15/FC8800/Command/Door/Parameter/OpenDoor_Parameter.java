package Net.PC15.FC8800.Command.Door.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.Data.DoorPortDetail;
public class OpenDoor_Parameter extends CommandParameter {
	public DoorPortDetail Door = new DoorPortDetail((short) 4);
	public boolean IsCheckNum = false;
	public int CheckNum = 1;
	public OpenDoor_Parameter(CommandDetail detail) {
		super(detail);
	}
}
