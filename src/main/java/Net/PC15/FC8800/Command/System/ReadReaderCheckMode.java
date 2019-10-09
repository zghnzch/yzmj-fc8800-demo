package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadReaderCheckMode_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;
public class ReadReaderCheckMode extends FC8800Command {
	public ReadReaderCheckMode(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 10, 137);
	}
	protected void Release0() {
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 1, 10, 137, 1)) {
			ByteBuf buf = model.GetDatabuff();
			ReadReaderCheckMode_Result ret = new ReadReaderCheckMode_Result();
			this._Result = ret;
			ret.ReaderCheckMode = buf.readUnsignedByte();
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
}
