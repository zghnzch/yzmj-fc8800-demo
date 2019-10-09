package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadSmogAlarmState_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;
public class ReadSmogAlarmState extends FC8800Command {
	public ReadSmogAlarmState(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 12, 18);
	}
	protected void Release0() {
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 1, 12, 18, 1)) {
			ByteBuf buf = model.GetDatabuff();
			ReadSmogAlarmState_Result ret = new ReadSmogAlarmState_Result();
			this._Result = ret;
			ret.SmogAlarm = buf.readUnsignedByte();
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
}
