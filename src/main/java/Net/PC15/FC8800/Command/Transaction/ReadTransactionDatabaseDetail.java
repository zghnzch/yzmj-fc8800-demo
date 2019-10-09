package Net.PC15.FC8800.Command.Transaction;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.Transaction.Result.ReadTransactionDatabaseDetail_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;
public class ReadTransactionDatabaseDetail extends FC8800Command {
	public ReadTransactionDatabaseDetail(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(8, 1);
	}
	protected void Release0() {
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 8, 1, 0, 78)) {
			ByteBuf buf = model.GetDatabuff();
			ReadTransactionDatabaseDetail_Result r = new ReadTransactionDatabaseDetail_Result();
			r.DatabaseDetail.SetBytes(buf);
			this._Result = r;
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
}
