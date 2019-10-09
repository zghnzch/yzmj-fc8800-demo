package Net.PC15.FC8800.Command.System;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteSN_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WriteSN_Broadcast extends FC8800Command {
	private static final byte[] DataStrt = new byte[]{-4, 101, 101, 51, -1};
	private static final byte[] DataEnd = new byte[]{-49, 53, -110};
	public WriteSN_Broadcast(WriteSN_Parameter par) {
		this._Parameter = par;
		String SN = par.GetSN();
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(18);
		dataBuf.writeBytes(DataStrt);
		dataBuf.writeBytes(SN.getBytes());
		dataBuf.writeBytes(DataEnd);
		this.CreatePacket(193, 209, 247, 18, dataBuf);
	}
	protected void Release0() {
	}
}
