package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.FC8800Command;
public class SendFireAlarm extends FC8800Command {
	public SendFireAlarm(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 12, 0);
	}
	protected void Release0() {
	}
}
