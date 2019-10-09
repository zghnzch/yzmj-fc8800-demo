package Net.PC15.FC8800.Command.Door.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.Data.DoorPortDetail;
public class RemoteDoor_Parameter extends CommandParameter {
	public DoorPortDetail Door = new DoorPortDetail((short) 4);
	public RemoteDoor_Parameter(CommandDetail detail) {
		super(detail);
	}
}
