package Net.PC15.FC8800.Command;
import Net.PC15.Command.AbstractCommand;
import Net.PC15.Command.E_CommandStatus;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.FC8800Identity;
import Net.PC15.FC8800.Packet.FC8800Decompile;
import Net.PC15.FC8800.Packet.FC8800PacketCompile;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Packet.INPacketModel;
import Net.PC15.Util.StringUtil;
import io.netty.buffer.ByteBuf;
public abstract class FC8800Command extends AbstractCommand {
	public static final short DoorMax = 4;
	public static String NULLPassword = "FFFFFFFF";
	public FC8800Command() {
		this._Decompile = new FC8800Decompile();
	}
	protected void CreatePacket(int iCmdType, int iCmdIndex) {
		this.CreatePacket(iCmdType, iCmdIndex, 0, 0, null);
	}
	protected void CreatePacket(int iCmdType, int iCmdIndex, int iCmdPar) {
		this.CreatePacket(iCmdType, iCmdIndex, iCmdPar, 0, null);
	}
	protected void CreatePacket(int iCmdType, int iCmdIndex, int iCmdPar, int iDataLen, ByteBuf Databuf) {
		FC8800Identity identity = (FC8800Identity) this._Parameter.getCommandDetail().Identity;
		String sn = identity.GetSN();
		String pwd = identity.GetPassword();
		sn = StringUtil.FillString(sn, 16, "0");
		pwd = StringUtil.FillString(pwd, 8, "F");
		long lPassword = Long.parseLong(pwd, 16);
		if (this._Packet != null) {
			this._Packet.Release();
		}
		this._Packet = new FC8800PacketCompile(sn, lPassword, (short) iCmdType, (short) iCmdIndex, (short) iCmdPar, iDataLen, Databuf);
		this.CommandReady();
	}
	@Override
	protected boolean CommandStep(INConnectorEvent oEvent, INPacketModel model) {
		FC8800PacketModel fcmodel = (FC8800PacketModel) model;
		FC8800PacketModel fcSendModel = (FC8800PacketModel) this._Packet.GetPacket();
		if (fcmodel.GetCode() != fcSendModel.GetCode()) {
			return false;
		}
		else if (this.CheckResponse_PasswordErr(fcmodel)) {
			this.CommandOver();
			this.RaisePasswordErrorEvent(oEvent);
			return false;
		}
		else if (this.CheckResponse_CheckSumErr(fcmodel)) {
			this._Status = E_CommandStatus.OnReady;
			if (this._SendCount > 100) {
				this.CommandOver();
				this.RaiseChecksumErrorEvent(oEvent);
			}
			return false;
		}
		else {
			return this._CommandStep(oEvent, fcmodel);
		}
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponseOK(model)) {
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
	protected boolean CheckResponseOK(FC8800PacketModel model) {
		return model.GetCmdType() == 33 && model.GetCmdIndex() == 1;
	}
	protected boolean CheckResponse_PasswordErr(FC8800PacketModel model) {
		return model.GetCmdType() == 33 && model.GetCmdIndex() == 2;
	}
	protected boolean CheckResponse_CheckSumErr(FC8800PacketModel model) {
		return model.GetCmdType() == 33 && model.GetCmdIndex() == 3;
	}
	protected boolean CheckResponse_Cmd(FC8800PacketModel model, int CmdType, int CmdIndex) {
		return model.GetCmdType() == 48 + CmdType && model.GetCmdIndex() == CmdIndex;
	}
	protected boolean CheckResponse_Cmd(FC8800PacketModel model, int CmdType, int CmdIndex, int CmdPar) {
		return model.GetCmdType() == 48 + CmdType && model.GetCmdIndex() == CmdIndex && model.GetCmdPar() == CmdPar;
	}
	protected boolean CheckResponse_Cmd(FC8800PacketModel model, int CmdType, int CmdIndex, int CmdPar, int DataLen) {
		return model.GetCmdType() == 48 + CmdType && model.GetCmdIndex() == CmdIndex && model.GetCmdPar() == CmdPar && model.GetDataLen() == (long) DataLen;
	}
}
