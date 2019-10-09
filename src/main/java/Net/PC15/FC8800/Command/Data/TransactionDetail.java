package Net.PC15.FC8800.Command.Data;
import Net.PC15.Data.INData;
import io.netty.buffer.ByteBuf;
public class TransactionDetail implements INData {
	public long DataBaseMaxSize;
	public long WriteIndex;
	public long ReadIndex;
	public boolean IsCircle;
	public int GetDataLen() {
		return 13;
	}
	public void SetBytes(ByteBuf data) {
		this.DataBaseMaxSize = data.readUnsignedInt();
		this.WriteIndex = data.readUnsignedInt();
		this.ReadIndex = data.readUnsignedInt();
		this.IsCircle = data.readBoolean();
	}
	public ByteBuf GetBytes() {
		return null;
	}
	public long readable() {
		if (this.IsCircle) {
			return this.DataBaseMaxSize;
		}
		else {
			if (this.WriteIndex > this.DataBaseMaxSize) {
				this.WriteIndex = 0L;
			}
			if (this.ReadIndex > this.DataBaseMaxSize) {
				this.ReadIndex = 0L;
			}
			if (this.ReadIndex == this.WriteIndex) {
				return 0L;
			}
			else if (this.WriteIndex > this.ReadIndex) {
				return this.WriteIndex - this.ReadIndex;
			}
			else {
				return this.WriteIndex < this.ReadIndex ? this.WriteIndex + (this.DataBaseMaxSize - this.ReadIndex) : 0L;
			}
		}
	}
	public long NewSzie() {
		return this.readable();
	}
}
