package Net.PC15.FC8800.Command.System.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.Data.DoorPortDetail;
public class WriteLockInteraction_Parameter extends CommandParameter {
	public final DoorPortDetail DoorPort = new DoorPortDetail((short) 4);
	public WriteLockInteraction_Parameter(CommandDetail detail) {
		super(detail);
	}
}
