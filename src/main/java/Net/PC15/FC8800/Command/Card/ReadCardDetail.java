package Net.PC15.FC8800.Command.Card;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Card.Parameter.ReadCardDetail_Parameter;
import Net.PC15.FC8800.Command.Card.Result.ReadCardDetail_Result;
import Net.PC15.FC8800.Command.Data.CardDetail;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class ReadCardDetail extends FC8800Command {
	public ReadCardDetail(ReadCardDetail_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(5);
		dataBuf.writeByte(0);
		dataBuf.writeInt(Integer.valueOf(par.CardData));
		this.CreatePacket(7, 3, 1, 5, dataBuf);
	}
	protected void Release0() {
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 7, 3, 1, 33)) {
			ByteBuf buf = model.GetDatabuff();
			ReadCardDetail_Result r = new ReadCardDetail_Result();
			buf.markReaderIndex();
			if (buf.readUnsignedByte() == 255) {
				r.IsReady = false;
			}
			else {
				r.IsReady = true;
				buf.resetReaderIndex();
				CardDetail cd = new CardDetail();
				cd.SetBytes(buf);
				r.Card = cd;
			}
			this._Result = r;
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
}
