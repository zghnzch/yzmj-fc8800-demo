package Net.PC15.FC8800.Packet;
import Net.PC15.Packet.INPacketModel;
import Net.PC15.Util.ByteUtil;
import Net.PC15.Util.StringUtil;
import Net.PC15.Util.UInt32Util;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import org.apache.log4j.Logger;
public class FC8800PacketModel implements INPacketModel {
	private static final Logger myLog = Logger.getRootLogger();
	private String _SN;
	private long _Password = 4294967295L;
	private long _Code = UInt32Util.GetRndNum();
	private short _CmdType;
	private short _CmdIndex;
	private short _CmdPar;
	private long _DataLen;
	private ByteBuf _CmdData;
	private short _Check;
	public String GetSN() {
		return this._SN;
	}
	public void SetSN(String sn) {
		if (!StringUtil.IsNullOrEmpty(sn)) {
			if (StringUtil.IsAscii(sn)) {
				if (sn.length() == 16) {
					this._SN = sn;
				}
			}
		}
	}
	public long GetPassword() {
		return this._Password;
	}
	public void SetPassword(long password) {
		if (UInt32Util.Check(password)) {
			this._Password = password;
		}
	}
	public long GetCode() {
		return this._Code;
	}
	public void SetCode(long code) {
		if (UInt32Util.Check(code)) {
			this._Code = code;
		}
	}
	public void SetCmdType(short iData) {
		if (ByteUtil.Check(iData)) {
			this._CmdType = iData;
		}
	}
	public short GetCmdType() {
		return this._CmdType;
	}
	public void SetCmdIndex(short iData) {
		if (ByteUtil.Check(iData)) {
			this._CmdIndex = iData;
		}
	}
	public short GetCmdIndex() {
		return this._CmdIndex;
	}
	public void SetCmdPar(short iData) {
		if (ByteUtil.Check(iData)) {
			this._CmdPar = iData;
		}
	}
	public short GetCmdPar() {
		return this._CmdPar;
	}
	public void SetDataLen(long iLen) {
		if (UInt32Util.Check(iLen)) {
			this._DataLen = iLen;
		}
	}
	public long GetDataLen() {
		return this._DataLen;
	}
	public void SetDatabuff(ByteBuf data) {
		this._CmdData = data;
	}
	public ByteBuf GetDatabuff() {
		// myLog.info("122D35760BA4450BAA5204E99A9F4312："+ ByteBufUtil.hexDump(this._CmdData).toUpperCase());
		return this._CmdData;
	}
	public void SetPacketCheck(short iData) {
		if (ByteUtil.Check(iData)) {
			this._Check = iData;
		}
	}
	public int GetPacketCheck() {
		return this._Check;
	}
	@Override
	public void Release() {
		if (this._CmdData != null) {
			if (this._CmdData.refCnt() > 0) {
				this._CmdData.release();
			}
			this._CmdData = null;
		}
	}
}
