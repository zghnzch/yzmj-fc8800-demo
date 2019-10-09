package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadVersion_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;
public class ReadVersion extends FC8800Command {
	public ReadVersion(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 8);
	}
	protected void Release0() {
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 1, 8, 0, 4)) {
			ByteBuf buf = model.GetDatabuff();
			byte[] strBuf1 = new byte[2];
			byte[] strBuf2 = new byte[2];
			String ver;
			try {
				buf.readBytes(strBuf1, 0, 2);
				buf.readBytes(strBuf2, 0, 2);
				ver = new String(strBuf1) + "." + new String(strBuf2);
			}
			catch (Exception var8) {
				ver = null;
			}
			this._Result = new ReadVersion_Result(ver);
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
}
