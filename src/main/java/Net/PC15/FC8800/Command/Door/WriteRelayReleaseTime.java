package Net.PC15.FC8800.Command.Door;
import Net.PC15.FC8800.Command.Door.Parameter.WriteRelayReleaseTime_Parameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WriteRelayReleaseTime extends FC8800Command {
	public WriteRelayReleaseTime(WriteRelayReleaseTime_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(3);
		dataBuf.writeByte(par.Door);
		dataBuf.writeShort(par.ReleaseTime);
		this.CreatePacket(3, 8, 1, 3, dataBuf);
	}
	protected void Release0() {
	}
}
