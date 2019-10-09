package Net.PC15.Packet;
import io.netty.buffer.ByteBuf;
public interface INPacket {
	ByteBuf GetPacketData();
	INPacketModel GetPacket();
	void SetPacket(INPacketModel var1);
	void Release();
}
