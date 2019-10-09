package Net.PC15.FC8800.Command.System.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.Data.TCPDetail;
public class WriteTCPSetting_Parameter extends CommandParameter {
	private TCPDetail _TCP;
	public WriteTCPSetting_Parameter(CommandDetail detail, TCPDetail tcp) {
		super(detail);
		this.SetTCP(tcp);
	}
	public TCPDetail GetTCP() {
		return this._TCP;
	}
	public void SetTCP(TCPDetail tcp) {
		this._TCP = tcp;
	}
}
