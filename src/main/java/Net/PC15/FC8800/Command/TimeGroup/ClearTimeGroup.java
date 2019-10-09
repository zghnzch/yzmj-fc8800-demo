package Net.PC15.FC8800.Command.TimeGroup;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.FC8800Command;
public class ClearTimeGroup extends FC8800Command {
	public ClearTimeGroup(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(6, 1);
	}
	protected void Release0() {
	}
}
