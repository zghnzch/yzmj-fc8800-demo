package Net.PC15.FC8800.Command.Door.Result;
import Net.PC15.Command.INCommandResult;
import Net.PC15.FC8800.Command.Data.DoorPortDetail;
public class ReadReaderOption_Result implements INCommandResult {
	public DoorPortDetail door = new DoorPortDetail((short) 4);
	public void release() {
	}
}
