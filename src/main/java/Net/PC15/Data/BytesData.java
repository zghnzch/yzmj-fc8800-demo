package Net.PC15.Data;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class BytesData implements INData {
	private ByteBuf _Buf;
	public int GetDataLen() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	public void SetBytes(ByteBuf data) {
		data.markReaderIndex();
		this._Buf = ByteUtil.ALLOCATOR.buffer(data.readableBytes());
		this._Buf.writeBytes(data);
		data.resetReaderIndex();
	}
	public ByteBuf GetBytes() {
		return this._Buf;
	}
}
