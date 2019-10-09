package Net.PC15.FC8800.Command.System.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Util.UInt32Util;
public class WriteDeadline_Parameter extends CommandParameter {
	private int _Deadline;
	public WriteDeadline_Parameter(CommandDetail detail, int Deadline) {
		super(detail);
		this.SetDeadline(Deadline);
	}
	public int GetDeadline() {
		return this._Deadline;
	}
	public void SetDeadline(int Deadline) {
		if (!UInt32Util.CheckNum(Deadline, 0, 65535)) {
			this._Deadline = 0;
		}
		else {
			this._Deadline = Deadline;
		}
	}
}
