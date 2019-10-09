package Net.PC15.Command;
import Net.PC15.Connector.ConnectorDetail;
import Net.PC15.Connector.INConnectorEvent;
import io.netty.buffer.ByteBuf;
public interface INWatchResponse {
	void Release();
	void CheckResponse(ConnectorDetail var1, INConnectorEvent var2, ByteBuf var3);
}
