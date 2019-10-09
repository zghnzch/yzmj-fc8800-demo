package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadSystemStatus_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Util.TimeUtil;
import io.netty.buffer.ByteBuf;
public class ReadSystemStatus extends FC8800Command {
	public ReadSystemStatus(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 9);
	}
	protected void Release0() {
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 1, 9, 0, 18)) {
			ByteBuf buf = model.GetDatabuff();
			ReadSystemStatus_Result rst = new ReadSystemStatus_Result();
			this._Result = rst;
			rst.RunDay = buf.readUnsignedShort();
			rst.FormatCount = buf.readUnsignedShort();
			rst.RestartCount = buf.readUnsignedShort();
			rst.UPS = buf.readUnsignedByte();
			byte tmp = buf.readByte();
			rst.Temperature = buf.readUnsignedByte();
			if (tmp == 0) {
				rst.Temperature = -rst.Temperature;
			}
			byte[] btData = new byte[7];
			buf.readBytes(btData, 0, 7);
			btData[5] = btData[6];
			rst.StartTime = TimeUtil.BCDTimeToDate_ssmmhhddMMyy(btData);
			buf.readBytes(btData, 0, 2);
			rst.Voltage = Float.parseFloat(String.format("%d.%d", btData[0], btData[1]));
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
}
