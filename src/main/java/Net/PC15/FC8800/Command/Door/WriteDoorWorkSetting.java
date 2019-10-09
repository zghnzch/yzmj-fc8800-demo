package Net.PC15.FC8800.Command.Door;
import Net.PC15.FC8800.Command.Door.Parameter.WriteDoorWorkSetting_Parameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class WriteDoorWorkSetting extends FC8800Command {
	public WriteDoorWorkSetting(WriteDoorWorkSetting_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(229);
		dataBuf.writeByte(par.DoorNum);
		dataBuf.writeBoolean(par.Use);
		dataBuf.writeByte(par.DoorWorkType);
		dataBuf.writeByte(par.HoldDoorOption);
		dataBuf.writeByte(0);
		par.timeGroup.GetBytes(dataBuf);
		this.CreatePacket(3, 6, 1, 229, dataBuf);
	}
	protected void Release0() {
	}
}
