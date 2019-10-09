package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadKeyboard_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;
public class ReadKeyboard extends FC8800Command {
	public ReadKeyboard(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 10, 130);
	}
	protected void Release0() {
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 1, 10, 130, 1)) {
			ByteBuf buf = model.GetDatabuff();
			ReadKeyboard_Result ret = new ReadKeyboard_Result();
			this._Result = ret;
			ret.Keyboard = buf.readUnsignedByte();
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
}
