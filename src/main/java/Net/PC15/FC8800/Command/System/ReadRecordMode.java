package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadRecordMode_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;
public class ReadRecordMode extends FC8800Command {
	public ReadRecordMode(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 10, 129);
	}
	protected void Release0() {
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 1, 10, 129, 1)) {
			ByteBuf buf = model.GetDatabuff();
			ReadRecordMode_Result ret = new ReadRecordMode_Result();
			this._Result = ret;
			ret.RecordMode = buf.readUnsignedByte();
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
}
