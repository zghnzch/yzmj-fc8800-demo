package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Data.TCPDetail;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadTCPSetting_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;
public class ReadTCPSetting extends FC8800Command {
	public ReadTCPSetting(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 6);
	}
	protected void Release0() {
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 1, 6, 0, 137)) {
			ByteBuf buf = model.GetDatabuff();
			TCPDetail tcp = new TCPDetail();
			tcp.SetBytes(buf);
			this._Result = new ReadTCPSetting_Result(tcp);
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
}
