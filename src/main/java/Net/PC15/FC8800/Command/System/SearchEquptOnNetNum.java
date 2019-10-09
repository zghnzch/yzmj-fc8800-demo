package Net.PC15.FC8800.Command.System;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.SearchEquptOnNetNum_Parameter;
import Net.PC15.FC8800.Command.System.Result.SearchEquptOnNetNum_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class SearchEquptOnNetNum extends FC8800Command {
	public SearchEquptOnNetNum(SearchEquptOnNetNum_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(2);
		dataBuf.writeShort(par.NetNum);
		this.CreatePacket(1, 254, 0, 2, dataBuf);
		this._Result = new SearchEquptOnNetNum_Result();
	}
	protected void Release0() {
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 1, 254, 0)) {
			SearchEquptOnNetNum_Result ret = (SearchEquptOnNetNum_Result) this._Result;
			byte[] ResultData = null;
			String SN = model.GetSN();
			if (model.GetDataLen() > 0L) {
				ByteBuf buf = model.GetDatabuff();
				ResultData = new byte[buf.readableBytes()];
				buf.readBytes(ResultData);
			}
			ret.AddSearchResult(SN, ResultData);
			return false;
		}
		else {
			return false;
		}
	}
}
