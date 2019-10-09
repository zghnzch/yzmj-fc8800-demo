package Net.PC15.FC8800.Command.Transaction;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.Transaction.Parameter.WriteTransactionDatabaseReadIndex_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WriteTransactionDatabaseReadIndex extends FC8800Command {
	public WriteTransactionDatabaseReadIndex(WriteTransactionDatabaseReadIndex_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(6);
		dataBuf.writeByte(par.DatabaseType.getValue());
		dataBuf.writeInt(par.ReadIndex);
		dataBuf.writeBoolean(par.IsCircle);
		this.CreatePacket(8, 3, 0, 6, dataBuf);
	}
	protected void Release0() {
	}
}
