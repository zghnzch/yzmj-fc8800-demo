package Net.PC15.FC8800.Command.System.Result;
import Net.PC15.Command.INCommandResult;
import Net.PC15.FC8800.Command.Data.TCPDetail;
public class ReadTCPSetting_Result implements INCommandResult {
	public final TCPDetail TCP;
	public ReadTCPSetting_Result(TCPDetail tcp) {
		this.TCP = tcp;
	}
	public void release() {
	}
}
