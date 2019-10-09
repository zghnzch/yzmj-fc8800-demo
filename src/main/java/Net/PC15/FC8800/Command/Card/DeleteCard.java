package Net.PC15.FC8800.Command.Card;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Card.Parameter.DeleteCard_Parameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Packet.FC8800PacketCompile;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Util.ByteUtil;
import Net.PC15.Util.StringUtil;
import io.netty.buffer.ByteBuf;

import java.math.BigInteger;
public class DeleteCard extends FC8800Command {
	protected int mIndex;
	protected String[] _List;
	public DeleteCard() {
	}
	public DeleteCard(DeleteCard_Parameter par) {
		this._Parameter = par;
		this._List = par.CardList;
		this._ProcessMax = this._List.length;
		this.mIndex = 0;
		int iLen = 204;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(iLen);
		this.CreatePacket(7, 5, 0, iLen, dataBuf);
		this.WriteNext();
	}
	private void WriteNext() {
		int iMaxSize = 40;
		int iSize = 0;
		int iIndex = 0;
		int ListLen = this._List.length;
		FC8800PacketCompile compile = (FC8800PacketCompile) this._Packet;
		FC8800PacketModel p = (FC8800PacketModel) this._Packet.GetPacket();
		ByteBuf dataBuf = p.GetDatabuff();
		dataBuf.clear();
		dataBuf.writeInt(iMaxSize);
		int i = this.mIndex;
		while (i < ListLen) {
			iIndex = i;
			++iSize;
			dataBuf.writeByte(0);
			String cardData = this._List[i];
			if (cardData != null && cardData.length() != 0) {
				if (!StringUtil.CanParseInt(cardData)) {
					System.out.println("ERROR! 卡号不是数字格式!");
					return;
				}
				BigInteger biLongMax = new BigInteger(String.valueOf(Long.MAX_VALUE));
				BigInteger biCardData = new BigInteger(cardData);
				if (biLongMax.compareTo(biCardData) <= 0) {
					System.out.println("ERROR! 卡号超过最大值!");
					return;
				}
				String CardDataHex = (new BigInteger(cardData, 10)).toString(16);
				CardDataHex = StringUtil.FillString(CardDataHex, 8, "0", false);
				StringUtil.HextoByteBuf(CardDataHex, dataBuf);
				if (iSize != iMaxSize) {
					++i;
					continue;
				}
				break;
			}
			System.out.println("ERROR! 卡号不能为空!");
			return;
		}
		if (iSize != iMaxSize) {
			dataBuf.setInt(0, iSize);
		}
		p.SetDataLen(dataBuf.readableBytes());
		compile.Compile();
		this.mIndex = iIndex + 1;
		this.CommandReady();
	}
	protected void Release0() {
		this._Parameter = null;
		this._Result = null;
		this._List = null;
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponseOK(model)) {
			this.CommandNext(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
	protected void CommandNext(INConnectorEvent oEvent) {
		this._ProcessStep = this.mIndex;
		if (this.mIndex < this._List.length) {
			this.WriteNext();
		}
		else {
			this.RaiseCommandCompleteEvent(oEvent);
		}
	}
}
