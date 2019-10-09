package Net.PC15.FC8800.Command.System;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadConnectPassword_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
public class ReadConnectPassword extends FC8800Command {
	private static final byte[] DataStrt = new byte[]{70, 67, 97, 114, 100, 89, 122};
	public ReadConnectPassword(CommandParameter par) {
		this._Parameter = par;
		ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(7);
		dataBuf.writeBytes(DataStrt);
		this.CreatePacket(1, 4, 0, 7, dataBuf);
	}
	protected void Release0() {
	}
	protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
		if (this.CheckResponse_Cmd(model, 1, 4, 0, 4)) {
			ByteBuf buf = model.GetDatabuff();
			byte[] PwdBuf = new byte[4];
			String pwd;
			try {
				buf.readBytes(PwdBuf);
				pwd = ByteUtil.ByteToHex(PwdBuf);
			}
			catch (Exception var7) {
				pwd = null;
			}
			this._Result = new ReadConnectPassword_Result(pwd);
			this.RaiseCommandCompleteEvent(oEvent);
			return true;
		}
		else {
			return false;
		}
	}
}
