package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadFireAlarmState_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;
public class ReadFireAlarmState extends FC8800Command {
	public ReadFireAlarmState(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 12, 2);
	}
	protected void Release0() {
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 1, 12, 2, 1)) {
			ByteBuf buf = model.GetDatabuff();
			ReadFireAlarmState_Result ret = new ReadFireAlarmState_Result();
			this._Result = ret;
			ret.FireAlarm = buf.readUnsignedByte();
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
}
