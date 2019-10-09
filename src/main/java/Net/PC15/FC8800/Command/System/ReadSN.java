package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadSN_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;
public class ReadSN extends FC8800Command {
	public ReadSN(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 2);
	}
	@Override
	protected void Release0() {
	}
	@Override
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 1, 2, 0, 16)) {
			ByteBuf buf = model.GetDatabuff();
			byte[] SNBuf = new byte[16];
			String SN;
			try {
				buf.readBytes(SNBuf);
				SN = new String(SNBuf, "GB2312");
			}
			catch (Exception var7) {
				SN = null;
			}
			this._Result = new ReadSN_Result(SN);
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
}
