package Net.PC15.FC89H.Command.Card;
import Net.PC15.FC8800.Command.Card.Parameter.ReadCardDataBase_Parameter;
import Net.PC15.FC8800.Command.Card.Result.ReadCardDataBase_Result;
import Net.PC15.FC89H.Command.Data.CardDetail;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
public class ReadCardDataBase extends Net.PC15.FC8800.Command.Card.ReadCardDataBase {
	public ReadCardDataBase(ReadCardDataBase_Parameter par) {
		super(par);
	}
	protected void Analysis(int iCardSize) throws Exception {
		ReadCardDataBase_Result result = (ReadCardDataBase_Result) this._Result;
		result.DataBaseSize = iCardSize;
		ArrayList CardList = new ArrayList(iCardSize);
		result.CardList = CardList;
		while (this.mBufs.peek() != null) {
			ByteBuf buf = (ByteBuf) this.mBufs.poll();
			iCardSize = buf.readInt();
			if ((buf.capacity() - 4) % 37 != 0) {
				buf.release();
				throw new Exception("数据流长度不正确");
			}
			for (int i = 0; i < iCardSize; ++i) {
				CardDetail cd = new CardDetail();
				cd.SetBytes(buf);
				CardList.add(cd);
			}
			buf.release();
		}
	}
}
