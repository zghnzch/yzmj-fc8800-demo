package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.FC8800Command;
public class FormatController extends FC8800Command {
	public FormatController(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 15, 0);
	}
	protected void Release0() {
	}
}
