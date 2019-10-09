package Net.PC15.FC8800.Command.System;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteReadCardSpeak_Parameter;
import io.netty.buffer.ByteBuf;
public class WriteReadCardSpeak extends FC8800Command {
	public WriteReadCardSpeak(WriteReadCardSpeak_Parameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = par.SpeakSetting.GetBytes();
		this.CreatePacket(1, 10, 6, 10, dataBuf);
	}
	protected void Release0() {
	}
}
