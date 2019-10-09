package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Data.TheftAlarmSetting;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadTheftAlarmSetting_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;
public class ReadTheftAlarmSetting extends FC8800Command {
	public ReadTheftAlarmSetting(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 10, 142);
	}
	protected void Release0() {
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 1, 10, 142, 13)) {
			ByteBuf buf = model.GetDatabuff();
			ReadTheftAlarmSetting_Result ret = new ReadTheftAlarmSetting_Result();
			this._Result = ret;
			TheftAlarmSetting ts = new TheftAlarmSetting();
			ts.SetBytes(buf);
			ret.Setting = ts;
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
}
