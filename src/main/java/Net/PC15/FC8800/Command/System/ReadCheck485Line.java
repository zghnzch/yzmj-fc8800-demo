package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadCheck485Line_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;
public class ReadCheck485Line extends FC8800Command {
	public ReadCheck485Line(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 19, 1);
	}
	protected void Release0() {
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 1, 19, 1, 1)) {
			ByteBuf buf = model.GetDatabuff();
			ReadCheck485Line_Result ret = new ReadCheck485Line_Result();
			this._Result = ret;
			int v = buf.readUnsignedByte();
			ret.Use = v == 1;
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
}
