package Net.PC15.FC8800.Command.Card;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Data.CardDetail;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
public abstract class DeleteCardBase extends FC8800Command {
	protected int mIndex;
	protected CardDetail[] _List;
	protected abstract void WriteNext();
	protected void Release0() {
		this._Parameter = null;
		this._Result = null;
		this._List = null;
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponseOK(model)) {
			this.CommandNext(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
	protected void CommandNext(INConnectorEvent oEvent) {
		this._ProcessStep = this.mIndex;
		if (this.mIndex < this._List.length) {
			this.WriteNext();
		}
		else {
			this.RaiseCommandCompleteEvent(oEvent);
		}
	}
}
