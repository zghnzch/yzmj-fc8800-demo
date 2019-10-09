package Net.PC15.FC8800.Command.System.Result;
import Net.PC15.Command.INCommandResult;
import Net.PC15.FC8800.Command.Data.*;
public class ReadAllSystemSetting_Result implements INCommandResult {
	public short RecordMode;
	public short Keyboard;
	public DoorPortDetail LockInteraction;
	public short FireAlarmOption;
	public short OpenAlarmOption;
	public int ReaderIntervalTime;
	public BroadcastDetail SpeakOpen;
	public short ReaderCheckMode;
	public short BuzzerMode;
	public short SmogAlarmOption;
	public DoorLimit EnterDoorLimit;
	public TheftAlarmSetting TheftAlarmPar;
	public short CheckInOut;
	public short CardPeriodSpeak;
	public ReadCardSpeak ReadCardSpeak;
	public void release() {
	}
}
