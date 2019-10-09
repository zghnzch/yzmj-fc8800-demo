package Net.PC15.FC8800.Command.System.Result;
import Net.PC15.Command.INCommandResult;
public class ReadConnectPassword_Result implements INCommandResult {
	public final String Password;
	public ReadConnectPassword_Result(String pwd) {
		this.Password = pwd;
	}
	public void release() {
	}
}
