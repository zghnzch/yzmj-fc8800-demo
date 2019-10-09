package Net.PC15.FC8800.Command.Transaction;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.Transaction.Parameter.WriteTransactionDatabaseWriteIndex_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WriteTransactionDatabaseWriteIndex extends FC8800Command {
	public WriteTransactionDatabaseWriteIndex(WriteTransactionDatabaseWriteIndex_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(5);
		dataBuf.writeByte(par.DatabaseType.getValue());
		dataBuf.writeInt(par.WriteIndex);
		this.CreatePacket(8, 3, 1, 5, dataBuf);
	}
	protected void Release0() {
	}
}
