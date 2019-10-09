package Net.PC15.Command;
import Net.PC15.Connector.ConnectorDetail;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.Packet.INPacketDecompile;
import Net.PC15.Packet.INPacketModel;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
public abstract class AbstractWatchResponse implements INWatchResponse {
	final INPacketDecompile _Decompile;
	protected AbstractWatchResponse(INPacketDecompile decompile) {
		this._Decompile = decompile;
	}
	public void Release() {
		this._Decompile.ClearBuf();
		this._Decompile.Release();
	}
	public void CheckResponse(ConnectorDetail connectorDetail, INConnectorEvent oEvent, ByteBuf bData) {
		ArrayList oRetPack = new ArrayList(10);
		boolean decompile = false;
		bData.markReaderIndex();
		decompile = this._Decompile.Decompile(bData, oRetPack);
		if (decompile) {
			int iLen = oRetPack.size();
			for (int i = 0; i < iLen; ++i) {
				INPacketModel model = (INPacketModel) oRetPack.get(i);
				this.fireWatchEvent(connectorDetail, oEvent, model);
				model.Release();
			}
		}
		bData.resetReaderIndex();
	}
	protected abstract void fireWatchEvent(ConnectorDetail var1, INConnectorEvent var2, INPacketModel var3);
}
