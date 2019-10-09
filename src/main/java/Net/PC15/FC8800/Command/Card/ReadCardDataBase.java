package Net.PC15.FC8800.Command.Card;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Card.Parameter.ReadCardDataBase_Parameter;
import Net.PC15.FC8800.Command.Card.Result.ReadCardDataBase_Result;
import Net.PC15.FC8800.Command.Card.Result.ReadCardDatabaseDetail_Result;
import Net.PC15.FC8800.Command.Data.CardDetail;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
public class ReadCardDataBase extends FC8800Command {
	protected int mStep;
	protected ConcurrentLinkedQueue mBufs;
	protected int mRecordCardSize;
	public ReadCardDataBase() {
	}
	public ReadCardDataBase(ReadCardDataBase_Parameter par) {
		this._Parameter = par;
		this._ProcessMax = 2;
		this._ProcessStep = 1;
		this.mStep = 1;
		this.CreatePacket(7, 1);
	}
	protected void Release0() {
		this.ClearBuf();
		this.mBufs = null;
		this._Parameter = null;
		this._Result = null;
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		switch (this.mStep) {
			case 1:
				return this.CheckDataBaseDetail(oEvent, model);
			case 2:
				return this.CheckReadCardPacket(oEvent, model);
			default:
				return false;
		}
	}
	protected boolean CheckDataBaseDetail(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 7, 1, 0, 16)) {
			ByteBuf buf = model.GetDatabuff();
			ReadCardDatabaseDetail_Result r = new ReadCardDatabaseDetail_Result();
			r.SortDataBaseSize = buf.readUnsignedInt();
			r.SortCardSize = buf.readUnsignedInt();
			r.SequenceDataBaseSize = buf.readUnsignedInt();
			r.SequenceCardSize = buf.readUnsignedInt();
			ReadCardDataBase_Parameter par = (ReadCardDataBase_Parameter) this._Parameter;
			ReadCardDataBase_Result result = new ReadCardDataBase_Result(par.CardType);
			this._Result = result;
			this.mRecordCardSize = 0;
			switch (par.CardType) {
				case 1:
					if (r.SortCardSize > 0L) {
						this.mRecordCardSize = (int) r.SortCardSize;
					}
					break;
				case 2:
					if (r.SequenceCardSize > 0L) {
						this.mRecordCardSize = (int) r.SequenceCardSize;
					}
					break;
				case 3:
					if (r.SortCardSize + r.SequenceCardSize > 0L) {
						this.mRecordCardSize = (int) (r.SortCardSize + r.SequenceCardSize);
					}
			}
			if (this.mRecordCardSize > 0) {
				this._ProcessMax = this.mRecordCardSize;
				this._ProcessStep = 0;
				buf = ByteUtil.ALLOCATOR.buffer(1);
				buf.writeByte(par.CardType);
				this.CreatePacket(7, 3, 0, 1, buf);
				this.CommandWaitResponse();
				this.mBufs = new ConcurrentLinkedQueue();
				this.mStep = 2;
			}
			else {
				this.RaiseCommandCompleteEvent(oEvent);
			}
			return true;
		}
		else {
			return false;
		}
	}
	protected boolean CheckReadCardPacket(INConnectorEvent oEvent, FC8800PacketModel model) {
		ByteBuf buf;
		int iCardSize;
		if (this.CheckResponse_Cmd(model, 7, 3, 0)) {
			try {
				buf = model.GetDatabuff();
				iCardSize = buf.getInt(0);
				this._ProcessStep += iCardSize;
				this.RaiseCommandProcessEvent(oEvent);
				buf.retain();
				this.mBufs.add(buf);
				this.CommandWaitResponse();
			}
			catch (Exception var6) {
				System.out.println("发送错误：" + var6.getLocalizedMessage());
			}
			return true;
		}
		else if (!this.CheckResponse_Cmd(model, 7, 3, 255, 4)) {
			return false;
		}
		else {
			buf = model.GetDatabuff();
			iCardSize = buf.readInt();
			if (iCardSize <= 0 && this.mBufs.isEmpty()) {
				this.RaiseCommandCompleteEvent(oEvent);
			}
			else {
				try {
					this.Analysis(iCardSize);
				}
				catch (Exception var7) {
				}
				this.RaiseCommandCompleteEvent(oEvent);
			}
			return true;
		}
	}
	protected void Analysis(int iCardSize) throws Exception {
		ReadCardDataBase_Result result = (ReadCardDataBase_Result) this._Result;
		if (iCardSize == 0) {
			iCardSize = 1024;
		}
		ArrayList CardList = new ArrayList(iCardSize);
		result.CardList = CardList;
		while (this.mBufs.peek() != null) {
			ByteBuf buf = (ByteBuf) this.mBufs.poll();
			iCardSize = buf.readInt();
			for (int i = 0; i < iCardSize; ++i) {
				CardDetail cd = new CardDetail();
				cd.SetBytes(buf);
				CardList.add(cd);
				++iCardSize;
			}
			buf.release();
		}
		result.DataBaseSize = CardList.size();
	}
	@Override
	protected void CommandOver_ReSend() {
		this.ClearBuf();
		this._ProcessMax = this.mRecordCardSize;
		this._ProcessStep = 0;
	}
	protected void ClearBuf() {
		if (this.mBufs != null) {
			while (this.mBufs.peek() != null) {
				ByteBuf buf = (ByteBuf) this.mBufs.poll();
				buf.release();
			}
		}
	}
}
