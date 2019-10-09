package Net.PC15.Command;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.Packet.INPacket;
import io.netty.buffer.ByteBuf;
public interface INCommandRuntime {
	void Release();
	boolean getIsWaitResponse();
	boolean CheckResponse(INConnectorEvent var1, ByteBuf var2);
	boolean CheckTimeout(INConnectorEvent var1);
	INPacket GetPacket();
	E_CommandStatus GetStatus();
	void SendCommand(INConnectorEvent var1);
	boolean getIsCommandOver();
	INIdentity GetIdentity();
	void RaiseCommandProcessEvent(INConnectorEvent var1);
	void RaiseCommandCompleteEvent(INConnectorEvent var1);
}
