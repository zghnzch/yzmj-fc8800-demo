package Net.PC15.FC8800.Command.System.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.Data.DoorLimit;
public class WriteEnterDoorLimit_Parameter extends CommandParameter {
	public DoorLimit Limit;
	public WriteEnterDoorLimit_Parameter(CommandDetail detail) {
		super(detail);
	}
}
