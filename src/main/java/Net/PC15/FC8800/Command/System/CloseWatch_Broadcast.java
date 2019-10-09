package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.FC8800Command;
public class CloseWatch_Broadcast extends FC8800Command {
	public CloseWatch_Broadcast(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 11, 17);
		this._IsWaitResponse = false;
	}
	protected void Release0() {
	}
}
