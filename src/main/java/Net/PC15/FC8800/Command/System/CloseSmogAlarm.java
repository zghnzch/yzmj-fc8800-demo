package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.FC8800Command;
public class CloseSmogAlarm extends FC8800Command {
	public CloseSmogAlarm(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 12, 17);
	}
	protected void Release0() {
	}
}
