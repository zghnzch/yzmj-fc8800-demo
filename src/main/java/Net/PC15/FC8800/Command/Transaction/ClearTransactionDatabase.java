package Net.PC15.FC8800.Command.Transaction;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.Transaction.Parameter.ClearTransactionDatabase_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class ClearTransactionDatabase extends FC8800Command {
	public ClearTransactionDatabase(ClearTransactionDatabase_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
		dataBuf.writeByte(par.DatabaseType.getValue());
		this.CreatePacket(8, 2, 1, 1, dataBuf);
	}
	protected void Release0() {
	}
}
