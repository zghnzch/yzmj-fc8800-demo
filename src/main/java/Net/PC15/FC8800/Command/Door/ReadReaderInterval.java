package Net.PC15.FC8800.Command.Door;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Door.Parameter.DoorPort_Parameter;
import Net.PC15.FC8800.Command.Door.Result.ReadReaderInterval_Result;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class ReadReaderInterval extends FC8800Command {
	public ReadReaderInterval(DoorPort_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
		dataBuf.writeByte(par.Door);
		this.CreatePacket(3, 9, 0, 1, dataBuf);
	}
	protected void Release0() {
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 3, 9, 0, 3)) {
			ByteBuf buf = model.GetDatabuff();
			ReadReaderInterval_Result r = new ReadReaderInterval_Result();
			r.DoorNum = buf.readByte();
			r.Use = buf.readBoolean();
			r.RecordOption = buf.readByte();
			this._Result = r;
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
}
