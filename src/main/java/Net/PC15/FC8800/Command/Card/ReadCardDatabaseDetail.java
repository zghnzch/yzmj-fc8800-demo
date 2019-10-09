package Net.PC15.FC8800.Command.Card;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Card.Result.ReadCardDatabaseDetail_Result;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;
public class ReadCardDatabaseDetail extends FC8800Command {
	public ReadCardDatabaseDetail(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(7, 1);
	}
	@Override
	protected void Release0() {
	}
	@Override
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 7, 1, 0, 16)) {
			ByteBuf buf = model.GetDatabuff();
			ReadCardDatabaseDetail_Result r = new ReadCardDatabaseDetail_Result();
			r.SortDataBaseSize = buf.readUnsignedInt();
			r.SortCardSize = buf.readUnsignedInt();
			r.SequenceDataBaseSize = buf.readUnsignedInt();
			r.SequenceCardSize = buf.readUnsignedInt();
			this._Result = r;
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
}
