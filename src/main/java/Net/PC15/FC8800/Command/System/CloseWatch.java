package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.FC8800Command;
public class CloseWatch extends FC8800Command {
	public CloseWatch(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 11, 1);
	}
	protected void Release0() {
	}
}
