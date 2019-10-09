package Net.PC15.Data;
import io.netty.buffer.ByteBuf;
public interface INData {
	int GetDataLen();
	void SetBytes(ByteBuf var1);
	ByteBuf GetBytes();
}
