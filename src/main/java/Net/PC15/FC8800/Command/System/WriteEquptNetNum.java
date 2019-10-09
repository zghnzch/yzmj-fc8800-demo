package Net.PC15.FC8800.Command.System;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.SearchEquptOnNetNum_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WriteEquptNetNum extends FC8800Command {
	public WriteEquptNetNum(SearchEquptOnNetNum_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(2);
		dataBuf.writeShort(par.NetNum);
		this.CreatePacket(1, 254, 1, 2, dataBuf);
	}
	protected void Release0() {
	}
}
