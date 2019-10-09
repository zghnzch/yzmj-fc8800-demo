package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Data.*;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadAllSystemSetting_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;
public class ReadAllSystemSetting extends FC8800Command {
	public ReadAllSystemSetting(CommandParameter par) {
		this._Parameter = par;
		this.CreatePacket(1, 10, 255);
	}
	protected void Release0() {
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (!this.CheckResponse_Cmd(model, 1, 10, 255, 92)) {
			return false;
		}
		else {
			ByteBuf buf = model.GetDatabuff();
			ReadAllSystemSetting_Result ret = new ReadAllSystemSetting_Result();
			this._Result = ret;
			ret.RecordMode = buf.readUnsignedByte();
			ret.Keyboard = buf.readUnsignedByte();
			byte[] btTmp = new byte[4];
			buf.readBytes(btTmp, 0, 4);
			buf.readBytes(btTmp, 0, 4);
			DoorPortDetail door = new DoorPortDetail((short) 4);
			for (int i = 0; i < 4; ++i) {
				door.SetDoor(i + 1, btTmp[i]);
			}
			ret.LockInteraction = door;
			ret.FireAlarmOption = buf.readUnsignedByte();
			ret.OpenAlarmOption = buf.readUnsignedByte();
			ret.ReaderIntervalTime = buf.readUnsignedShort();
			btTmp = new byte[10];
			buf.readBytes(btTmp, 0, 10);
			BroadcastDetail Broadcast = new BroadcastDetail(btTmp);
			ret.SpeakOpen = Broadcast;
			ret.ReaderCheckMode = buf.readUnsignedByte();
			ret.BuzzerMode = buf.readUnsignedByte();
			ret.SmogAlarmOption = buf.readUnsignedByte();
			DoorLimit dl = new DoorLimit();
			dl.GlobalLimit = buf.readUnsignedInt();
			int i;
			for (i = 0; i < 4; ++i) {
				dl.DoorLimit[i] = buf.readUnsignedInt();
			}
			dl.GlobalEnter = buf.readUnsignedInt();
			for (i = 0; i < 4; ++i) {
				dl.DoorEnter[i] = buf.readUnsignedInt();
			}
			ret.EnterDoorLimit = dl;
			TheftAlarmSetting ts = new TheftAlarmSetting();
			ts.SetBytes(buf);
			ret.TheftAlarmPar = ts;
			ret.CheckInOut = buf.readUnsignedByte();
			ret.CardPeriodSpeak = buf.readUnsignedByte();
			ReadCardSpeak sp = new ReadCardSpeak();
			sp.SetBytes(buf);
			ret.ReadCardSpeak = sp;
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
	}
}
