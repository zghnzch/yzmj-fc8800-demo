package Net.PC15.FC8800.Packet;
import Net.PC15.Packet.INPacket;
import Net.PC15.Packet.INPacketModel;
import Net.PC15.Util.ByteUtil;
import Net.PC15.Util.StringUtil;
import Net.PC15.Util.UInt32Util;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import org.apache.log4j.Logger;
public class FC8800PacketCompile implements INPacket {
	private static final Logger myLog = Logger.getRootLogger();
	private FC8800PacketModel _Packet = null;
	private ByteBuf _PacketData;
	private long _CheckSum = 0L;
	public FC8800PacketCompile() {
	}
	public FC8800PacketCompile(String sn, long pwd, short iCmdType, short iCmdIndex, short iCmdPar) {
		long lSource = UInt32Util.GetRndNum();
		this.CreateFrame(sn, pwd, lSource, iCmdType, iCmdIndex, iCmdPar, 0, null);
	}
	public FC8800PacketCompile(String sn, long pwd, short iCmdType, short iCmdIndex, short iCmdPar, int iDataLen, ByteBuf Databuf) {
		long lSource = UInt32Util.GetRndNum();
		this.CreateFrame(sn, pwd, lSource, iCmdType, iCmdIndex, iCmdPar, iDataLen, Databuf);
	}
	public FC8800PacketCompile(String sn, long pwd, long code, short iCmdType, short iCmdIndex, short iCmdPar, int iDataLen, ByteBuf Databuf) {
		this.CreateFrame(sn, pwd, code, iCmdType, iCmdIndex, iCmdPar, iDataLen, Databuf);
	}
	private void CreateFrame(String sn, long pwd, long code, short iCmdType, short iCmdIndex, short iCmdPar, int iDataLen, ByteBuf Databuf) {
		if(this._Packet != null) {
			this._Packet.Release();
		}
		this._Packet = new FC8800PacketModel();
		this._Packet.SetSN(sn);
		this._Packet.SetPassword(pwd);
		this._Packet.SetCode(code);
		this._Packet.SetCmdType(iCmdType);
		this._Packet.SetCmdIndex(iCmdIndex);
		this._Packet.SetCmdPar(iCmdPar);
		this._Packet.SetDataLen(iDataLen);
		if(iDataLen > 0) {
			this._Packet.SetDatabuff(Databuf);
		}
		// myLog.info("调试卡号错误2："+ByteBufUtil.hexDump(Databuf).toUpperCase());
		this.Compile();
	}
	public void Compile() {
		if(this._PacketData != null && this._PacketData.refCnt() > 0) {
			this._PacketData.release();
		}
		long iLen = 34L + this._Packet.GetDataLen();
		iLen *= 2L;
		this._PacketData = ByteUtil.ALLOCATOR.buffer((int) iLen);
		// myLog.info("DFC577247426423AB731BA81CC9E3E50："+ByteBufUtil.hexDump(this._PacketData).toUpperCase());
		this._PacketData.writeByte(126);
		// myLog.info("8EADE0B68C394DAB8922329EF9872B4E："+ByteBufUtil.hexDump(this._PacketData).toUpperCase());
		this._CheckSum = 0L;
		this.Push(StringUtil.FillString(this._Packet.GetSN(), 16, "F").getBytes());
		// myLog.info("70D73D6E4745463D94C91BA6D0431E55："+ByteBufUtil.hexDump(this._PacketData).toUpperCase());
		this.Push(UInt32Util.UINT32ToByteBuf(this._Packet.GetPassword()));
		this.Push(UInt32Util.UINT32ToByteBuf(this._Packet.GetCode()));
		// myLog.info("2E3398D005A14073AB1DE20560B6DB6E："+ByteBufUtil.hexDump(this._PacketData).toUpperCase());
		this.Push(this._Packet.GetCmdType());
		this.Push(this._Packet.GetCmdIndex());
		this.Push(this._Packet.GetCmdPar());
		// myLog.info("EA96B060CC8144EE90418541A7388D10："+ByteBufUtil.hexDump(this._PacketData).toUpperCase());
		this.Push(UInt32Util.UINT32ToByteBuf(this._Packet.GetDataLen()));
		// myLog.info("6DD0746C4A784008AAE880A81C2BD76F："+ByteBufUtil.hexDump(this._PacketData).toUpperCase());
		if(this._Packet.GetDataLen() > 0L) {
			// myLog.info("285430189E52481EB7AC6C819E644363："+ByteBufUtil.hexDump(this._PacketData).toUpperCase());
			this.Push(this._Packet.GetDatabuff(), false);
			// myLog.info("670D0E1778864656B05DD9BEA194F33C："+ByteBufUtil.hexDump(this._PacketData).toUpperCase());
		}
		// myLog.info("78C4B0877B05488495B7D2DD4E9EDA2B："+ByteBufUtil.hexDump(this._PacketData).toUpperCase());
		short sum = (short) ((int) (this._CheckSum & 255L));
		// myLog.info("178B5ACED63440FD9E432A2BFF995DEC："+ByteBufUtil.hexDump(this._PacketData).toUpperCase());
		this._Packet.SetPacketCheck(sum);
		// myLog.info("BF75D2593A094375ACF61F8AE4D67300："+ByteBufUtil.hexDump(this._PacketData).toUpperCase());
		this.Push(sum);
		this._CheckSum = sum;
		this._PacketData.writeByte(126);

		// myLog.info("620E8259ECA7434A80C0DB3688C6C3AB："+ByteBufUtil.hexDump(this._PacketData).toUpperCase());
	}
	private void Push(int iByte) {
		this._CheckSum += iByte;
		if(iByte == 126) {
			this._PacketData.writeByte(127);
			this._PacketData.writeByte(1);
		}
		else if(iByte == 127) {
			this._PacketData.writeByte(127);
			this._PacketData.writeByte(2);
		}
		else {
			this._PacketData.writeByte(iByte);
		}
	}
	private void Push(byte[] buf) {
		byte[] var2 = buf;
		int var3 = buf.length;
		for (int var4 = 0; var4 < var3; ++var4) {
			byte b = var2[var4];
			int num = ByteUtil.uByte(b);
			this.Push(num);
		}
	}
	private void Push(ByteBuf buf) {
		this.Push(buf, true);
	}
	private void Push(ByteBuf buf, boolean release) {
		buf.forEachByte((value) -> {
			this.Push(ByteUtil.uByte(value));
			return true;
		});
		if(release) {
			buf.release();
		}
		buf = null;
	}
	@Override
	public INPacketModel GetPacket() {
		return this._Packet;
	}
	@Override
	public void SetPacket(INPacketModel packet) {
		this._Packet = (FC8800PacketModel) packet;
		if(this._Packet == null) {
			this.Release();
		}
		this.Compile();
	}
	@Override
	public ByteBuf GetPacketData() {
		// myLog.info("调试门禁卡号错误1：" + ByteBufUtil.hexDump(this._PacketData).toUpperCase());
		return this._PacketData;
	}
	@Override
	public void Release() {
		if(this._PacketData != null) {
			if(this._PacketData.refCnt() > 0) {
				this._PacketData.release();
			}
			this._PacketData = null;
		}
		if(this._Packet != null) {
			this._Packet.Release();
			this._Packet = null;
		}
	}
}
