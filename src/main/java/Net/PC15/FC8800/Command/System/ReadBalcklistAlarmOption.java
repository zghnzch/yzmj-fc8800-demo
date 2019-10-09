package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadBalcklistAlarmOption_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;
public class ReadBalcklistAlarmOption extends FC8800Command {
	public ReadBalcklistAlarmOption(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 18, 1);
	}
	protected void Release0() {
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 1, 18, 1, 1)) {
			ByteBuf buf = model.GetDatabuff();
			ReadBalcklistAlarmOption_Result ret = new ReadBalcklistAlarmOption_Result();
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
