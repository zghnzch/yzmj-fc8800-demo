package Net.PC15.FC8800.Command.Door;
import Net.PC15.FC8800.Command.Door.Parameter.WriteAnyCardSetting_Parameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WriteAnyCardSetting extends FC8800Command {
	public WriteAnyCardSetting(WriteAnyCardSetting_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(4);
		dataBuf.writeByte(par.DoorNum);
		dataBuf.writeBoolean(par.Use);
		dataBuf.writeBoolean(par.AutoSave);
		dataBuf.writeByte(par.AutoSaveTimeGroupIndex);
		this.CreatePacket(3, 17, 0, 4, dataBuf);
	}
	protected void Release0() {
	}
}
