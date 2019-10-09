package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Data.BroadcastDetail;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadBroadcast_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;
public class ReadBroadcast extends FC8800Command {
	public ReadBroadcast(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 10, 136);
	}
	protected void Release0() {
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 1, 10, 136, 10)) {
			ByteBuf buf = model.GetDatabuff();
			ReadBroadcast_Result ret = new ReadBroadcast_Result();
			this._Result = ret;
			byte[] data = new byte[10];
			buf.readBytes(data, 0, 10);
			BroadcastDetail b = new BroadcastDetail(data);
			ret.Broadcast = b;
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
}
