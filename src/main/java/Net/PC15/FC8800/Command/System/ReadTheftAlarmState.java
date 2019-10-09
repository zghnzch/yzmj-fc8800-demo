package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadTheftAlarmState_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;
public class ReadTheftAlarmState extends FC8800Command {
	public ReadTheftAlarmState(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 14, 1);
	}
	protected void Release0() {
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 1, 14, 1, 2)) {
			ByteBuf buf = model.GetDatabuff();
			ReadTheftAlarmState_Result ret = new ReadTheftAlarmState_Result();
			this._Result = ret;
			ret.TheftState = buf.readByte();
			ret.TheftAlarm = buf.readByte();
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
}
