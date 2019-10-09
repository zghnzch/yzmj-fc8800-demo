package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.FC8800Command;
public class BeginWatch_Broadcast extends FC8800Command {
	public BeginWatch_Broadcast(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 11, 16);
		this._IsWaitResponse = false;
	}
	protected void Release0() {
	}
}
