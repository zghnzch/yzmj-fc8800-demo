package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadLockInteraction_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;
public class ReadLockInteraction extends FC8800Command {
	public ReadLockInteraction(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 10, 132);
	}
	protected void Release0() {
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (!this.CheckResponse_Cmd(model, 1, 10, 132, 4)) {
			return false;
		}
		else {
			ByteBuf buf = model.GetDatabuff();
			ReadLockInteraction_Result ret = new ReadLockInteraction_Result();
			this._Result = ret;
			byte[] btTmp = new byte[4];
			buf.readBytes(btTmp, 0, 4);
			for (int i = 1; i < 4; ++i) {
				ret.DoorPort.SetDoor(i, btTmp[i - 1]);
			}
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
	}
}
