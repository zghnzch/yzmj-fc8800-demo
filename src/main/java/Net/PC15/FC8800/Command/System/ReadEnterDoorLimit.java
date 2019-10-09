package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Data.DoorLimit;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadEnterDoorLimit_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;
public class ReadEnterDoorLimit extends FC8800Command {
	public ReadEnterDoorLimit(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 10, 140);
	}
	protected void Release0() {
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (!this.CheckResponse_Cmd(model, 1, 10, 140, 40)) {
			return false;
		}
		else {
			ByteBuf buf = model.GetDatabuff();
			ReadEnterDoorLimit_Result ret = new ReadEnterDoorLimit_Result();
			this._Result = ret;
			DoorLimit dl = new DoorLimit();
			dl.GlobalLimit = buf.readUnsignedInt();
			int i;
			for (i = 0; i < 4; ++i) {
				dl.DoorLimit[i] = buf.readUnsignedInt();
			}
			dl.GlobalEnter = buf.readUnsignedInt();
			for (i = 0; i < 4; ++i) {
				dl.DoorEnter[i] = buf.readUnsignedInt();
			}
			ret.Limit = dl;
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
	}
}
