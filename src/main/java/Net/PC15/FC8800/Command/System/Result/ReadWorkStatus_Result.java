package Net.PC15.FC8800.Command.System.Result;
import Net.PC15.Command.INCommandResult;
import Net.PC15.FC8800.Command.Data.DoorLimit;
import Net.PC15.FC8800.Command.Data.DoorPortDetail;
public class ReadWorkStatus_Result implements INCommandResult {
	public DoorPortDetail RelayState;
	public DoorPortDetail DoorLongOpenState;
	public DoorPortDetail DoorState;
	public DoorPortDetail DoorAlarmState;
	public int AlarmState;
	public DoorPortDetail LockState;
	public DoorPortDetail PortLockState;
	public int WatchState;
	public DoorLimit EnterTotal;
	public int TheftState;
	public void release() {
	}
}
