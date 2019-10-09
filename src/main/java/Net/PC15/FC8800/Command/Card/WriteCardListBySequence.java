//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package Net.PC15.FC8800.Command.Card;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Card.Parameter.WriteCardListBySequence_Parameter;
import Net.PC15.FC8800.Command.Card.Result.WriteCardListBySequence_Result;
import Net.PC15.FC8800.Command.Data.CardDetail;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Packet.FC8800PacketCompile;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
public class WriteCardListBySequence extends FC8800Command {
	protected int mIndex;
	protected ArrayList<? extends CardDetail> _List;
	protected ConcurrentLinkedQueue<ByteBuf> mBufs;
	public WriteCardListBySequence() {
	}
	public WriteCardListBySequence(WriteCardListBySequence_Parameter par) {
		this._Parameter = par;
		this._List = par.CardList;
		this._ProcessMax = par.CardList.size();
		this.mIndex = 0;
		this._CreatePacket();
		this.WriteNext();
	}
	protected void _CreatePacket() {
		int iLen = 169;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(iLen);
		this.CreatePacket(7, 4, 0, iLen, dataBuf);
	}
	protected void WriteNext() {
		int iMaxSize = 5;
		int iSize = 0;
		int iIndex = 0;
		int ListLen = this._List.size();
		FC8800PacketCompile compile = (FC8800PacketCompile) this._Packet;
		FC8800PacketModel p = (FC8800PacketModel) this._Packet.GetPacket();
		ByteBuf dataBuf = p.GetDatabuff();
		dataBuf.clear();
		dataBuf.writeInt(iMaxSize);
		for (int i = this.mIndex; i < ListLen; ++i) {
			iIndex = i;
			++iSize;
			CardDetail cd = this._List.get(i);
			cd.GetBytes(dataBuf);
			if (iSize == iMaxSize) {
				break;
			}
		}
		if (iSize != iMaxSize) {
			dataBuf.setInt(0, iSize);
		}
		p.SetDataLen(dataBuf.readableBytes());
		compile.Compile();
		this.mIndex = iIndex + 1;
		this.CommandReady();
	}
	@Override
	protected void Release0() {
		this.ClearBuf();
		this.mBufs = null;
		this._Parameter = null;
		this._Result = null;
		this._List = null;
	}
	@Override
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponseOK(model)) {
			this.CommandNext(oEvent);
			return true;
		}
		else if (this.CheckResponse_Cmd(model, 7, 4, 255)) {
			this.SaveBuf(model.GetDatabuff());
			this.CommandNext(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
	protected void CommandNext(INConnectorEvent oEvent) {
		this._ProcessStep = this.mIndex;
		if (this.mIndex < this._List.size()) {
			this.WriteNext();
		}
		else {
			WriteCardListBySequence_Result r = new WriteCardListBySequence_Result();
			this._Result = r;
			this.Analysis();
			this.RaiseCommandCompleteEvent(oEvent);
		}
	}
	protected void Analysis() {
		if (this.mBufs != null) {
			//int iCardSize = false;
			ArrayList<CardDetail> CardList = new ArrayList(10000);
			WriteCardListBySequence_Result r = (WriteCardListBySequence_Result) this._Result;
			while (this.mBufs.peek() != null) {
				ByteBuf buf = this.mBufs.poll();
				int iCardSize = buf.readInt();
				for (int i = 0; i < iCardSize; ++i) {
					CardDetail cd = new CardDetail();
					cd.SetBytes(buf);
					CardList.add(cd);
				}
				buf.release();
			}
			r.CardList = CardList;
			r.FailTotal = CardList.size();
		}
	}
	protected void SaveBuf(ByteBuf buf) {
		if (this.mBufs == null) {
			this.mBufs = new ConcurrentLinkedQueue();
		}
		buf.retain();
		this.mBufs.add(buf);
	}
	protected void ClearBuf() {
		if (this.mBufs != null) {
			while (this.mBufs.peek() != null) {
				ByteBuf buf = this.mBufs.poll();
				buf.release();
			}
		}
	}
}
