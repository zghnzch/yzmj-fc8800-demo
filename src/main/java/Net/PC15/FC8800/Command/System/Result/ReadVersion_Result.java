package Net.PC15.FC8800.Command.System.Result;
import Net.PC15.Command.INCommandResult;
public class ReadVersion_Result implements INCommandResult {
	public final String Version;
	public ReadVersion_Result(String ver) {
		this.Version = ver;
	}
	public void release() {
	}
}
