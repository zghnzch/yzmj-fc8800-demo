package Net.PC15.FC8800.Command.Door.Result;
import Net.PC15.Command.INCommandResult;
public class ReadAntiPassback_Result implements INCommandResult {
	public int DoorNum;
	public boolean Use;
	public void release() {
	}
}
