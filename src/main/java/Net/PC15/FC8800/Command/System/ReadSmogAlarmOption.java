package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadSmogAlarmOption_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;
public class ReadSmogAlarmOption extends FC8800Command {
	public ReadSmogAlarmOption(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 10, 139);
	}
	protected void Release0() {
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 1, 10, 139, 1)) {
			ByteBuf buf = model.GetDatabuff();
			ReadSmogAlarmOption_Result ret = new ReadSmogAlarmOption_Result();
			this._Result = ret;
			ret.Option = buf.readUnsignedByte();
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
}
