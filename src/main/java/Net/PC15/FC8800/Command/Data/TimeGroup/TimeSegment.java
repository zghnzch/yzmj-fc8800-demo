package Net.PC15.FC8800.Command.Data.TimeGroup;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class TimeSegment {
	protected short[] mBeginTime = new short[2];
	protected short[] mEndTime = new short[2];
	public void SetBeginTime(int Hour, int Minute) {
		this.CheckTime(Hour, Minute);
		this.mBeginTime[0] = (short) Hour;
		this.mBeginTime[1] = (short) Minute;
	}
	public void GetBeginTime(short[] time) {
		time[0] = this.mBeginTime[0];
		time[1] = this.mBeginTime[1];
	}
	public void SetEndTime(int Hour, int Minute) {
		this.CheckTime(Hour, Minute);
		this.mEndTime[0] = (short) Hour;
		this.mEndTime[1] = (short) Minute;
	}
	public void GetEndTime(short[] time) {
		time[0] = this.mEndTime[0];
		time[1] = this.mEndTime[1];
	}
	public String toString() {
		StringBuilder buf = new StringBuilder(15);
		buf.append(String.format("%02d", this.mBeginTime[0]));
		buf.append(":");
		buf.append(String.format("%02d", this.mBeginTime[1]));
		buf.append(" - ");
		buf.append(String.format("%02d", this.mEndTime[0]));
		buf.append(":");
		buf.append(String.format("%02d", this.mEndTime[1]));
		return buf.toString();
	}
	private void CheckTime(int Hour, int Minute) {
		if (Hour >= 0 && Hour <= 23) {
			if (Minute < 0 || Minute > 59) {
				throw new IllegalArgumentException("Minute -- 0-59");
			}
		}
		else {
			throw new IllegalArgumentException("Hour -- 0-23");
		}
	}
	public void GetBytes(ByteBuf bBuf) {
		bBuf.writeByte(ByteUtil.ByteToBCD((byte) this.mBeginTime[0]));
		bBuf.writeByte(ByteUtil.ByteToBCD((byte) this.mBeginTime[1]));
		bBuf.writeByte(ByteUtil.ByteToBCD((byte) this.mEndTime[0]));
		bBuf.writeByte(ByteUtil.ByteToBCD((byte) this.mEndTime[1]));
	}
	public void SetBytes(ByteBuf bBuf) {
		this.mBeginTime[0] = ByteUtil.BCDToByte(bBuf.readByte());
		this.mBeginTime[1] = ByteUtil.BCDToByte(bBuf.readByte());
		this.mEndTime[0] = ByteUtil.BCDToByte(bBuf.readByte());
		this.mEndTime[1] = ByteUtil.BCDToByte(bBuf.readByte());
	}
}
