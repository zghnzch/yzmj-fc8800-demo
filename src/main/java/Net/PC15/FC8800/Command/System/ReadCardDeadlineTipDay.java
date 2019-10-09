package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadCardDeadlineTipDay_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;
public class ReadCardDeadlineTipDay extends FC8800Command {
	public ReadCardDeadlineTipDay(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 21, 1);
	}
	protected void Release0() {
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 1, 21, 1, 1)) {
			ByteBuf buf = model.GetDatabuff();
			ReadCardDeadlineTipDay_Result ret = new ReadCardDeadlineTipDay_Result();
			this._Result = ret;
			ret.Day = buf.readUnsignedByte();
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
}
