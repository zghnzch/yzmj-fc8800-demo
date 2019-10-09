package Net.PC15.FC8800.Command.TimeGroup;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Data.TimeGroup.WeekTimeGroup;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.TimeGroup.Parameter.AddTimeGroup_Parameter;
import Net.PC15.FC8800.Packet.FC8800PacketCompile;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
public class AddTimeGroup extends FC8800Command {
	protected int mIndex;
	protected ArrayList List;
	public AddTimeGroup(AddTimeGroup_Parameter par) {
		this.List = par.List;
		this._Parameter = par;
		this._ProcessMax = this.List.size();
		this.mIndex = 0;
		this._CreatePacket();
		this.WriteNext();
	}
	protected void _CreatePacket() {
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(225);
		this.CreatePacket(6, 3, 0, 225, dataBuf);
	}
	protected void Release0() {
	}
	private void WriteNext() {
		FC8800PacketCompile compile = (FC8800PacketCompile) this._Packet;
		FC8800PacketModel p = (FC8800PacketModel) this._Packet.GetPacket();
		ByteBuf dataBuf = p.GetDatabuff();
		dataBuf.clear();
		dataBuf.writeByte(this.mIndex + 1);
		((WeekTimeGroup) this.List.get(this.mIndex)).GetBytes(dataBuf);
		p.SetDataLen(dataBuf.readableBytes());
		compile.Compile();
		++this.mIndex;
		this.CommandReady();
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponseOK(model)) {
			this.CommandNext(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
	protected void CommandNext(INConnectorEvent oEvent) {
		this._ProcessStep = this.mIndex;
		if (this.mIndex < this.List.size()) {
			this.WriteNext();
		}
	}
}
