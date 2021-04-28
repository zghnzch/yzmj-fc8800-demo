//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package Net.PC15.FC89H.Command.Card;
import Net.PC15.FC8800.Command.Card.Parameter.WriteCardListBySequence_Parameter;
import Net.PC15.FC8800.Command.Card.Result.WriteCardListBySequence_Result;
import Net.PC15.FC8800.Packet.FC8800PacketCompile;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.FC89H.Command.Data.CardDetail;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
public class WriteCardListBySequence extends Net.PC15.FC8800.Command.Card.WriteCardListBySequence {
	private static final Logger myLog = Logger.getRootLogger();
	public WriteCardListBySequence(WriteCardListBySequence_Parameter par) {
		this._Parameter = par;
		this._List = par.CardList;
		this._ProcessMax = par.CardList.size();
		this.mIndex = 0;
		int iLen = 189;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(iLen);
		this.CreatePacket(7, 4, 0, iLen, dataBuf);
		this.WriteNext();
	}
	@Override
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
		try {
			for (int i = this.mIndex; i < ListLen; ++i) {
				iIndex = i;
				++iSize;
				CardDetail cd = (CardDetail) this._List.get(i);
				cd.GetBytes(dataBuf);
				if(iSize == iMaxSize) {
					break;
				}
			}
			if(iSize != iMaxSize) {
				dataBuf.setInt(0, iSize);
			}
			p.SetDataLen(dataBuf.readableBytes());
			// myLog.info("调试卡号dataBuf：" + ByteBufUtil.hexDump(dataBuf).toUpperCase());
			compile.Compile();
			this.mIndex = iIndex + 1;
			this.CommandReady();
		}
		catch (Exception var10) {
		}
	}
	@Override
	protected void Analysis() {
		if(this.mBufs != null) {
			// int iCardSize = false;
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
			r.FailTotal = CardList.size();
			r.CardList = CardList;
		}
	}
}
