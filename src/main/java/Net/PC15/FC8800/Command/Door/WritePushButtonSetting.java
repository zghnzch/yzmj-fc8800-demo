package Net.PC15.FC8800.Command.Door;
import Net.PC15.FC8800.Command.Door.Parameter.WritePushButtonSetting_Parameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WritePushButtonSetting extends FC8800Command {
	public WritePushButtonSetting(WritePushButtonSetting_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(227);
		dataBuf.writeByte(par.DoorNum);
		dataBuf.writeBoolean(par.Use);
		dataBuf.writeBoolean(par.NormallyOpen);
		par.TimeGroup.GetBytes(dataBuf);
		this.CreatePacket(3, 15, 1, 227, dataBuf);
	}
	protected void Release0() {
	}
}
