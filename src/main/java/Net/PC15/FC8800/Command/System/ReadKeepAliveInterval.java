package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadKeepAliveInterval_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;
public class ReadKeepAliveInterval extends FC8800Command {
	public ReadKeepAliveInterval(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 240, 3);
	}
	protected void Release0() {
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 1, 240, 3, 2)) {
			ByteBuf buf = model.GetDatabuff();
			ReadKeepAliveInterval_Result ret = new ReadKeepAliveInterval_Result();
			this._Result = ret;
			ret.IntervalTime = buf.readUnsignedShort();
			this.RaiseCommandCompleteEvent(oEvent);
			return false;
		}
		else {
			return false;
		}
	}
}
