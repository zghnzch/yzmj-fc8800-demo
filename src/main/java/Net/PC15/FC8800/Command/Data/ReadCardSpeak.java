package Net.PC15.FC8800.Command.Data;
import Net.PC15.Data.INData;
import Net.PC15.Util.ByteUtil;
import Net.PC15.Util.TimeUtil;
import io.netty.buffer.ByteBuf;

import java.util.Calendar;
public class ReadCardSpeak implements INData {
	public boolean Use;
	public int MsgIndex;
	public Calendar BeginDate;
	public Calendar EndDate;
	public int GetDataLen() {
		return 10;
	}
	public void SetBytes(ByteBuf data) {
		this.Use = data.readBoolean();
		this.MsgIndex = data.readUnsignedByte();
		byte[] btData = new byte[4];
		data.readBytes(btData, 0, 4);
		this.BeginDate = TimeUtil.BCDTimeToDate_yyMMddhh(btData);
		data.readBytes(btData, 0, 4);
		this.EndDate = TimeUtil.BCDTimeToDate_yyMMddhh(btData);
	}
	public ByteBuf GetBytes() {
		ByteBuf buf = ByteUtil.ALLOCATOR.buffer(this.GetDataLen());
		buf.writeBoolean(this.Use);
		buf.writeByte(this.MsgIndex);
		byte[] btData = new byte[4];
		TimeUtil.DateToBCD_yyMMddhh(btData, this.BeginDate);
		buf.writeBytes(btData);
		TimeUtil.DateToBCD_yyMMddhh(btData, this.EndDate);
		buf.writeBytes(btData);
		return buf;
	}
}
