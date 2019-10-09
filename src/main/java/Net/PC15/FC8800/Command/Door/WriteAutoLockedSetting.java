package Net.PC15.FC8800.Command.Door;
import Net.PC15.FC8800.Command.Door.Parameter.WriteAutoLockedSetting_Parameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WriteAutoLockedSetting extends FC8800Command {
	public WriteAutoLockedSetting(WriteAutoLockedSetting_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(226);
		dataBuf.writeByte(par.DoorNum);
		dataBuf.writeBoolean(par.Use);
		par.timeGroup.GetBytes(dataBuf);
		this.CreatePacket(3, 7, 0, 226, dataBuf);
	}
	protected void Release0() {
	}
}
