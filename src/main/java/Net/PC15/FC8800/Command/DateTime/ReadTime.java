package Net.PC15.FC8800.Command.DateTime;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.DateTime.Result.ReadTime_Result;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Util.TimeUtil;
import io.netty.buffer.ByteBuf;
import org.apache.log4j.Logger;

import java.util.Calendar;
public class ReadTime extends FC8800Command {
	private static final Logger myLog = Logger.getLogger(ReadTime.class);
	public ReadTime(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(2, 1);
	}
	@Override
	protected void Release0() {
		myLog.info("read0");
	}
	@Override
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 2, 1, 0, 7)) {
			ByteBuf buf = model.GetDatabuff();
			byte[] datebyte = new byte[7];
			buf.readBytes(datebyte, 0, 7);
			datebyte[5] = datebyte[6];
			Calendar tDate = TimeUtil.BCDTimeToDate_ssmmhhddMMyy(datebyte);
			this._Result = new ReadTime_Result(tDate);
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
}
