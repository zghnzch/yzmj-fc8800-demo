package Net.PC15.FC8800.Command.System;
import Net.PC15.FC8800.Command.Data.TCPDetail;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteTCPSetting_Parameter;
public class WriteTCPSetting extends FC8800Command {
	public WriteTCPSetting(WriteTCPSetting_Parameter par) {
		this._Parameter = par;
		TCPDetail tcp = par.GetTCP();
		if (tcp == null) {
			throw new IllegalArgumentException("tcp is null");
		}
		else {
			this.CreatePacket(1, 6, 1, tcp.GetDataLen(), tcp.GetBytes());
		}
	}
	protected void Release0() {
	}
}
