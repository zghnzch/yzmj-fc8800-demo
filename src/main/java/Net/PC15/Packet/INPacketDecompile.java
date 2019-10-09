package Net.PC15.Packet;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
public interface INPacketDecompile {
	boolean Decompile(ByteBuf var1, ArrayList var2);
	void ClearBuf();
	void Release();
}
