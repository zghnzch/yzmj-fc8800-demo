package Net.PC15.FC8800.Command.Door;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Door.Result.ReadReaderOption_Result;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;
public class ReadReaderOption extends FC8800Command {
	public ReadReaderOption(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(3, 1);
	}
	protected void Release0() {
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (!this.CheckResponse_Cmd(model, 3, 1, 0, 4)) {
			return false;
		}
		else {
			ByteBuf buf = model.GetDatabuff();
			ReadReaderOption_Result r = new ReadReaderOption_Result();
			for (int i = 1; i <= 4; ++i) {
				r.door.SetDoor(i, buf.readByte());
			}
			this._Result = r;
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
	}
}
