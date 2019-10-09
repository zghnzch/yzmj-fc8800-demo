package Net.PC15.FC8800.Command.Data;
import Net.PC15.Data.INData;
import Net.PC15.Util.ByteUtil;
import Net.PC15.Util.StringUtil;
import io.netty.buffer.ByteBuf;
public class TheftAlarmSetting implements INData {
	public boolean Use;
	public int InTime;
	public int OutTime;
	public String BeginPassword;
	public String ClosePassword;
	public int AlarmTime;
	public int GetDataLen() {
		return 13;
	}
	public void SetBytes(ByteBuf data) {
		this.Use = data.readBoolean();
		this.InTime = data.readUnsignedByte();
		this.OutTime = data.readUnsignedByte();
		byte[] btPwd = new byte[4];
		data.readBytes(btPwd, 0, 4);
		this.BeginPassword = ByteUtil.ByteToHex(btPwd);
		data.readBytes(btPwd, 0, 4);
		this.ClosePassword = ByteUtil.ByteToHex(btPwd);
		this.AlarmTime = data.readUnsignedShort();
	}
	public ByteBuf GetBytes() {
		ByteBuf buf = ByteUtil.ALLOCATOR.buffer(this.GetDataLen());
		buf.writeBoolean(this.Use);
		buf.writeByte(this.InTime);
		buf.writeByte(this.OutTime);
		this.BeginPassword = StringUtil.FillHexString(this.BeginPassword, 8, "F", false);
		this.ClosePassword = StringUtil.FillHexString(this.ClosePassword, 8, "F", false);
		long pwd = Long.parseLong(this.BeginPassword, 16);
		buf.writeInt((int) pwd);
		pwd = Long.parseLong(this.ClosePassword, 16);
		buf.writeInt((int) pwd);
		buf.writeShort(this.AlarmTime);
		return buf;
	}
}
