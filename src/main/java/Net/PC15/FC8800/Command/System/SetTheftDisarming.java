package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.FC8800Command;
public class SetTheftDisarming extends FC8800Command {
	public SetTheftDisarming(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 17, 2);
	}
	protected void Release0() {
	}
}
