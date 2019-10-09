package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Data.ReadCardSpeak;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadReadCardSpeak_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;
public class ReadReadCardSpeak extends FC8800Command {
	public ReadReadCardSpeak(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 10, 145);
	}
	protected void Release0() {
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 1, 10, 145, 10)) {
			ByteBuf buf = model.GetDatabuff();
			ReadReadCardSpeak_Result ret = new ReadReadCardSpeak_Result();
			this._Result = ret;
			ReadCardSpeak SpeakSetting = new ReadCardSpeak();
			SpeakSetting.SetBytes(buf);
			ret.SpeakSetting = SpeakSetting;
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
}
