package Net.PC15.FC8800.Command.Data;
import Net.PC15.Data.AbstractTransaction;
import Net.PC15.Data.INData;
import io.netty.buffer.ByteBuf;
public class FC8800WatchTransaction implements INData {
	public short CmdType;
	public short CmdIndex;
	public short CmdPar;
	public String SN;
	public AbstractTransaction EventData;
	public int GetDataLen() {
		return 0;
	}
	public void SetBytes(ByteBuf data) {
	}
	public ByteBuf GetBytes() {
		return null;
	}
}
