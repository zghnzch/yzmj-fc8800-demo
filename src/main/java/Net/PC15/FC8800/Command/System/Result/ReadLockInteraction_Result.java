package Net.PC15.FC8800.Command.System.Result;
import Net.PC15.Command.INCommandResult;
import Net.PC15.FC8800.Command.Data.DoorPortDetail;
public class ReadLockInteraction_Result implements INCommandResult {
	public final DoorPortDetail DoorPort = new DoorPortDetail((short) 4);
	public void release() {
	}
}
