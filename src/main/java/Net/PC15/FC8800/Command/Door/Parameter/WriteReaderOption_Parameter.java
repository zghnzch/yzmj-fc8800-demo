package Net.PC15.FC8800.Command.Door.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.Data.DoorPortDetail;
public class WriteReaderOption_Parameter extends CommandParameter {
	public DoorPortDetail door = new DoorPortDetail((short) 4);
	public WriteReaderOption_Parameter(CommandDetail detail) {
		super(detail);
	}
}
