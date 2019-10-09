package Net.PC15.FC89H.Command.Card;
import Net.PC15.FC8800.Command.Card.Parameter.WriteCardListBySort_Parameter;
import Net.PC15.FC8800.Packet.FC8800PacketCompile;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.FC89H.Command.Data.CardDetail;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

import java.util.Collections;
public class WriteCardListBySort extends Net.PC15.FC8800.Command.Card.WriteCardListBySort {
	public WriteCardListBySort(WriteCardListBySort_Parameter par) {
		super(par);
	}
	@Override
	protected void IniWriteCard() {
		this._ProcessStep = 2;
		int iLen = 378;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(iLen);
		this.CreatePacket(7, 7, 1, iLen, dataBuf);
		this.mIndex = 0;
		Collections.sort(this._List);
		this.WriteNext();
		this.CommandReady();
		this.mStep = 3;
	}
	@Override
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
		try {
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
		catch (Exception var9) {
		}
	}
}
