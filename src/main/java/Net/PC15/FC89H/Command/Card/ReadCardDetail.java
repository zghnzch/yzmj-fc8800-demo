package Net.PC15.FC89H.Command.Card;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Card.Parameter.ReadCardDetail_Parameter;
import Net.PC15.FC8800.Command.Card.Result.ReadCardDetail_Result;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.FC89H.Command.Data.CardDetail;
import Net.PC15.Util.ByteUtil;
import Net.PC15.Util.StringUtil;
import io.netty.buffer.ByteBuf;
public class ReadCardDetail extends FC8800Command {
	public ReadCardDetail(ReadCardDetail_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(9);
		dataBuf.writeByte(0);
		StringUtil.HextoByteBuf(String.valueOf(par.CardData), dataBuf);
		this.CreatePacket(7, 3, 1, 9, dataBuf);
	}
	@Override
	protected void Release0() {
	}
	@Override
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 7, 3, 1, 37)) {
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
