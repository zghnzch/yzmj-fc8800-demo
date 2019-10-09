package Net.PC15.FC8800.Command.Card;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Card.Parameter.WriteCardListBySort_Parameter;
import Net.PC15.FC8800.Command.Card.Result.ReadCardDatabaseDetail_Result;
import Net.PC15.FC8800.Command.Card.Result.WriteCardListBySort_Result;
import Net.PC15.FC8800.Command.Data.CardDetail;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Packet.FC8800PacketCompile;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Collections;
public class WriteCardListBySort extends FC8800Command {
	protected int mIndex;
	protected ArrayList _List;
	protected int mStep;
	protected int mUploadMax;
	public WriteCardListBySort(WriteCardListBySort_Parameter par) {
		this._Parameter = par;
		this._List = par.CardList;
		this._ProcessMax = par.CardList.size();
		this.mIndex = 0;
		this.mUploadMax = this._List.size();
		this.CreatePacket(7, 1);
		this.mStep = 1;
	}
	protected void Release0() {
		this._Parameter = null;
		this._Result = null;
		this._List = null;
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		switch (this.mStep) {
			case 1:
				return this.CheckDataBaseDetail(model);
			case 2:
				return this.CheckBeginWriteResponse(model);
			case 3:
				return this.CheckWriteCardResponse(model);
			case 4:
				return this.CheckEndWriteResponse(oEvent, model);
			default:
				return false;
		}
	}
	private boolean CheckDataBaseDetail(FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 7, 1, 0, 16)) {
			ByteBuf buf = model.GetDatabuff();
			new ReadCardDatabaseDetail_Result();
			int iDataBaseSize = buf.readInt();
			if (iDataBaseSize < this.mUploadMax) {
				this.mUploadMax = iDataBaseSize;
			}
			this.SendBeginWrite();
			return true;
		}
		else {
			return false;
		}
	}
	private void SendBeginWrite() {
		this._ProcessStep = 1;
		this.CreatePacket(7, 7, 0);
		this.CommandReady();
		this.mStep = 2;
	}
	private boolean CheckBeginWriteResponse(FC8800PacketModel model) {
		if (this.CheckResponseOK(model)) {
			this.IniWriteCard();
			return true;
		}
		else {
			return false;
		}
	}
	protected void IniWriteCard() {
		this._ProcessStep = 2;
		int iLen = 338;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(iLen);
		this.CreatePacket(7, 7, 1, iLen, dataBuf);
		this.mIndex = 0;
		Collections.sort(this._List);
		this.WriteNext();
		this.CommandReady();
		this.mStep = 3;
	}
	protected void WriteNext() {
		int iMaxSize = 10;
		int iSize = 0;
		int iIndex = 0;
		FC8800PacketCompile compile = (FC8800PacketCompile) this._Packet;
		FC8800PacketModel p = (FC8800PacketModel) this._Packet.GetPacket();
		ByteBuf dataBuf = p.GetDatabuff();
		dataBuf.clear();
		dataBuf.writeInt(this.mIndex + 1);
		dataBuf.writeInt(iMaxSize);
		for (int i = this.mIndex; i < this.mUploadMax; ++i) {
			iIndex = i;
			++iSize;
			CardDetail cd = (CardDetail) this._List.get(i);
			cd.GetBytes(dataBuf);
			if (iSize == iMaxSize) {
				break;
			}
		}
		if (iSize != iMaxSize) {
			dataBuf.setInt(4, iSize);
		}
		p.SetDataLen(dataBuf.readableBytes());
		compile.Compile();
		this.mIndex = iIndex + 1;
		this.CommandReady();
	}
	private boolean CheckWriteCardResponse(FC8800PacketModel model) {
		if (!this.CheckResponseOK(model)) {
			return false;
		}
		else {
			this._ProcessStep = this.mIndex;
			if (this.mIndex < this.mUploadMax) {
				this.WriteNext();
			}
			else {
				WriteCardListBySort_Result r = new WriteCardListBySort_Result();
				int ListSize = this._List.size();
				if (this._List.size() > this.mUploadMax) {
					r.FailTotal = ListSize - this.mUploadMax;
					ArrayList failList = new ArrayList(r.FailTotal);
					for (int i = this.mIndex; i < ListSize; ++i) {
						failList.add(this._List.get(i));
					}
					r.CardList = failList;
				}
				this._Result = r;
				this.SendEndWrite();
			}
			return true;
		}
	}
	private void SendEndWrite() {
		this.CreatePacket(7, 7, 2);
		this.CommandReady();
		this.mStep = 4;
	}
	private boolean CheckEndWriteResponse(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponseOK(model)) {
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
}
