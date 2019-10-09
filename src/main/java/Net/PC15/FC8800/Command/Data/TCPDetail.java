//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package Net.PC15.FC8800.Command.Data;
import Net.PC15.Data.INData;
import Net.PC15.Util.ByteUtil;
import Net.PC15.Util.StringUtil;
import Net.PC15.Util.UInt32Util;
import io.netty.buffer.ByteBuf;
public class TCPDetail implements INData {
	private String mMAC;
	private String mIP;
	private String mIPMask;
	private String mIPGateway;
	private String mDNS;
	private String mDNSBackup;
	private short mProtocolType;
	private int mTCPPort;
	private int mUDPPort;
	private int mServerPort;
	private String mServerIP;
	private boolean mAutoIP;
	private String mServerAddr;
	public TCPDetail() {
	}
	public static boolean CheckMAC(String mac) {
		if (StringUtil.IsNullOrEmpty(mac)) {
			return false;
		}
		else {
			String[] macList = mac.split("-");
			int iLen = macList.length;
			if (iLen != 6) {
				return false;
			}
			else {
				try {
					for (int i = 0; i < iLen; ++i) {
						int iValue = Integer.parseInt(macList[i], 16);
						if (iValue < 0 || iValue > 255) {
							return false;
						}
					}
					return true;
				}
				catch (Exception var5) {
					return false;
				}
			}
		}
	}
	public static boolean CheckIP(String ip) {
		if (StringUtil.IsNullOrEmpty(ip)) {
			return false;
		}
		else {
			String[] ipList = ip.split("\\.");
			int iLen = ipList.length;
			if (iLen != 4) {
				return false;
			}
			else {
				try {
					for (int i = 0; i < iLen; ++i) {
						int iValue = Integer.parseInt(ipList[i]);
						if (iValue < 0 || iValue > 255) {
							return false;
						}
					}
					return true;
				}
				catch (Exception var5) {
					return false;
				}
			}
		}
	}
	public int GetDataLen() {
		return 137;
	}
	public void SetBytes(ByteBuf data) {
		StringBuilder strbuilder = new StringBuilder(30);
		strbuilder.reverse();
		int iLen;
		for (iLen = 0; iLen < 6; ++iLen) {
			short iMacValue = data.readUnsignedByte();
			String hex = Integer.toString(iMacValue, 16);
			strbuilder.append(hex);
			if (iLen < 5) {
				strbuilder.append("-");
			}
		}
		this.mMAC = strbuilder.toString();
		strbuilder.delete(0, strbuilder.length());
		this.ReadIPByByteBuf(data, strbuilder);
		this.mIP = strbuilder.toString();
		strbuilder.delete(0, strbuilder.length());
		this.ReadIPByByteBuf(data, strbuilder);
		this.mIPMask = strbuilder.toString();
		strbuilder.delete(0, strbuilder.length());
		this.ReadIPByByteBuf(data, strbuilder);
		this.mIPGateway = strbuilder.toString();
		strbuilder.delete(0, strbuilder.length());
		this.ReadIPByByteBuf(data, strbuilder);
		this.mDNS = strbuilder.toString();
		strbuilder.delete(0, strbuilder.length());
		this.ReadIPByByteBuf(data, strbuilder);
		this.mDNSBackup = strbuilder.toString();
		this.mProtocolType = data.readUnsignedByte();
		this.mTCPPort = data.readUnsignedShort();
		this.mUDPPort = data.readUnsignedShort();
		this.mServerPort = data.readUnsignedShort();
		strbuilder.delete(0, strbuilder.length());
		this.ReadIPByByteBuf(data, strbuilder);
		this.mServerIP = strbuilder.toString();
		this.mAutoIP = data.readByte() == 1;
		iLen = data.readableBytes();
		int iReadIndex = data.readerIndex();
		int iCharCount = 0;
		// byte bValue = false;
		for (int i = 0; i < iLen; ++i) {
			byte bValue = data.readByte();
			if (bValue == 0) {
				break;
			}
			++iCharCount;
		}
		data.readerIndex(iReadIndex);
		if (iCharCount == 0) {
			this.mServerAddr = null;
		}
		else {
			byte[] tmp = new byte[iCharCount];
			data.readBytes(tmp, 0, iCharCount);
			this.mServerAddr = new String(tmp);
		}
		strbuilder = null;
	}
	private void ReadIPByByteBuf(ByteBuf data, StringBuilder strbuilder) {
		for (int i = 0; i < 4; ++i) {
			strbuilder.append(data.readUnsignedByte());
			if (i < 3) {
				strbuilder.append(".");
			}
		}
	}
	public ByteBuf GetBytes() {
		ByteBuf buf = ByteUtil.ALLOCATOR.buffer(137);
		this.SaveMACToByteBuf(this.mMAC, buf);
		this.SaveIPToByteBuf(this.mIP, buf);
		this.SaveIPToByteBuf(this.mIPMask, buf);
		this.SaveIPToByteBuf(this.mIPGateway, buf);
		this.SaveIPToByteBuf(this.mDNS, buf);
		this.SaveIPToByteBuf(this.mDNSBackup, buf);
		buf.writeByte(this.mProtocolType);
		buf.writeShort(this.mTCPPort);
		buf.writeShort(this.mUDPPort);
		buf.writeShort(this.mServerPort);
		this.SaveIPToByteBuf(this.mServerIP, buf);
		buf.writeByte(ByteUtil.BoolToByte(this.mAutoIP));
		if (StringUtil.IsNullOrEmpty(this.mServerAddr)) {
			this.Save0toByteBuf(buf, 99);
		}
		else {
			byte[] tmp = this.mServerAddr.getBytes();
			buf.writeBytes(tmp);
			int iCount = 99 - tmp.length;
			if (iCount > 0) {
				this.Save0toByteBuf(buf, iCount);
			}
		}
		return buf;
	}
	private void Save0toByteBuf(ByteBuf buf, int iCount) {
		for (int i = 0; i < iCount; ++i) {
			buf.writeByte(0);
		}
	}
	private void SaveMACToByteBuf(String mac, ByteBuf buf) {
		if (!CheckMAC(mac)) {
			for (int i = 0; i < 6; ++i) {
				buf.writeByte(0);
			}
		}
		else {
			String[] macList = mac.split("-");
			int iLen = macList.length;
			for (int i = 0; i < iLen; ++i) {
				buf.writeByte(Integer.parseInt(macList[i], 16));
			}
		}
	}
	private void SaveIPToByteBuf(String IP, ByteBuf buf) {
		if (!CheckIP(IP)) {
			for (int i = 0; i < 4; ++i) {
				buf.writeByte(0);
			}
		}
		else {
			String[] ipList = IP.split("\\.");
			int iLen = ipList.length;
			for (int i = 0; i < iLen; ++i) {
				buf.writeByte(Integer.parseInt(ipList[i]));
			}
		}
	}
	public void SetMAC(String mac) {
		if (CheckMAC(mac)) {
			this.mMAC = mac;
		}
	}
	public String GetMAC() {
		return this.mMAC;
	}
	public void SetIP(String ip) {
		if (CheckIP(ip)) {
			this.mIP = ip;
		}
	}
	public String GetIP() {
		return this.mIP;
	}
	public void SetIPMask(String mask) {
		if (CheckIP(mask)) {
			this.mIPMask = mask;
		}
	}
	public String GetIPMask() {
		return this.mIPMask;
	}
	public void SetIPGateway(String gateway) {
		if (CheckIP(gateway)) {
			this.mIPGateway = gateway;
		}
	}
	public String GetIPGateway() {
		return this.mIPGateway;
	}
	public void SetDNS(String dns) {
		if (CheckIP(dns)) {
			this.mDNS = dns;
		}
	}
	public String GetDNS() {
		return this.mDNS;
	}
	public void SetDNSBackup(String dns) {
		if (CheckIP(dns)) {
			this.mDNSBackup = dns;
		}
	}
	public String GetDNSBackup() {
		return this.mDNSBackup;
	}
	public void SetProtocolType(short ProtocolType) {
		if (UInt32Util.CheckNum(ProtocolType, 1, 2)) {
			this.mProtocolType = ProtocolType;
		}
	}
	public short GetProtocolType() {
		return this.mProtocolType;
	}
	public void SetTCPPort(int port) {
		if (UInt32Util.CheckNum(port, 1, 65535)) {
			this.mTCPPort = port;
		}
	}
	public int GetTCPPort() {
		return this.mTCPPort;
	}
	public void SetUDPPort(int port) {
		if (UInt32Util.CheckNum(port, 1, 65535)) {
			this.mUDPPort = port;
		}
	}
	public int GetUDPPort() {
		return this.mUDPPort;
	}
	public void SetServerPort(int port) {
		if (UInt32Util.CheckNum(port, 1, 65535)) {
			this.mServerPort = port;
		}
	}
	public int GetServerPort() {
		return this.mServerPort;
	}
	public void SetServerIP(String ip) {
		if (CheckIP(ip)) {
			this.mServerIP = ip;
		}
	}
	public String GetServerIP() {
		return this.mServerIP;
	}
	public void SetAutoIP(boolean auto) {
		this.mAutoIP = auto;
	}
	public boolean GetAutoIP() {
		return this.mAutoIP;
	}
	public void SetServerAddr(String server) {
		if (StringUtil.IsNullOrEmpty(server)) {
			this.mServerAddr = null;
		}
		else if (server.length() > 99) {
			this.mServerAddr = server.substring(0, 99);
		}
		else {
			this.mServerAddr = server;
		}
	}
	public String GetServerAddr() {
		return this.mServerAddr;
	}
}
