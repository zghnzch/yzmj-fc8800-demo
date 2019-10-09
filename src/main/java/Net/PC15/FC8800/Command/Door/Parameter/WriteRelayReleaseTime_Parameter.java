package Net.PC15.FC8800.Command.Door.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
public class WriteRelayReleaseTime_Parameter extends CommandParameter {
	public int Door;
	public int ReleaseTime;
	public WriteRelayReleaseTime_Parameter(CommandDetail detail, int door, int time) {
		super(detail);
		this.Door = door;
		this.ReleaseTime = time;
	}
}
