package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.FC8800Command;
public class BeginWatch extends FC8800Command {
	public BeginWatch(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 11, 0);
	}
	protected void Release0() {
	}
}
