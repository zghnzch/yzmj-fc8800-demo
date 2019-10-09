package Net.PC15.FC8800.Command.System.Result;
import Net.PC15.Command.INCommandResult;
public class ReadSN_Result implements INCommandResult {
	public final String SN;
	public ReadSN_Result(String sn) {
		this.SN = sn;
	}
	public void release() {
	}
}
