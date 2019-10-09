package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.FC8800Command;
public class SendSmogAlarm extends FC8800Command {
	public SendSmogAlarm(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 12, 16);
	}
	protected void Release0() {
	}
}
