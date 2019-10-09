package Net.PC15.FC8800.Command.DateTime;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Packet.INPacket;
import Net.PC15.Util.ByteUtil;
import Net.PC15.Util.TimeUtil;
import io.netty.buffer.ByteBuf;

import java.util.Calendar;
public class WriteTime extends FC8800Command {
	public WriteTime(CommandParameter par) {
		this._Parameter = par;
	}
	@Override
	public INPacket GetPacket() {
		byte[] Datebuf = new byte[7];
		Calendar t = Calendar.getInstance();
		TimeUtil.DateToBCD_ssmmhhddMMwwyy(Datebuf, t);
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(7);
		dataBuf.writeBytes(Datebuf);
		if (this._Packet != null) {
			this._Packet.Release();
		}
		this._Packet = null;
		this.CreatePacket(2, 2, 0, 7, dataBuf);
		return this._Packet;
	}
	protected void Release0() {
	}
}
