package Net.PC15.FC8800.Command.Card;
import Net.PC15.FC8800.Command.Card.Parameter.ClearCardDataBase_Parameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class ClearCardDataBase extends FC8800Command {
	public ClearCardDataBase(ClearCardDataBase_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
		dataBuf.writeByte(par.CardType);
		this.CreatePacket(7, 2, 0, 1, dataBuf);
	}
	protected void Release0() {
	}
}
