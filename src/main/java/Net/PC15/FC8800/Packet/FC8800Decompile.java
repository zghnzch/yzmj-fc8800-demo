//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package Net.PC15.FC8800.Packet;
import Net.PC15.Packet.INPacketDecompile;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.ArrayList;
public class FC8800Decompile implements INPacketDecompile {
	public static final ByteBufAllocator DEFAULT_POOLED;
	static {
		DEFAULT_POOLED = ByteUtil.ALLOCATOR;
	}
	private final byte[] _SNBytes;
	private int _Step = 0;
	private ByteBuf _Buf;
	private boolean _Translate = false;
	private FC8800PacketModel _Packet = null;
	private long _Checksum = 0L;
	private ByteBuf _DataBuf;
	private int _DataLen;
	public FC8800Decompile() {
		this._Buf = DEFAULT_POOLED.buffer(32);
		this._SNBytes = new byte[16];
		this.ClearBuf();
	}
	@Override
	// public boolean Decompile(ByteBuf bDataBuf, ArrayList<INPacketModel> oRetPack) {
	public boolean Decompile(ByteBuf bDataBuf, ArrayList oRetPack) {
		bDataBuf.markReaderIndex();
		boolean bDecompile = false;
		boolean ret = false;
		int iBufLen = bDataBuf.readableBytes();
		for (int i = 0; i < iBufLen; ++i) {
			short num = bDataBuf.readUnsignedByte();
			ret = this.DecompileByte(num);
			if (ret) {
				bDecompile = true;
				oRetPack.add(this._Packet);
				this._Packet = null;
				this.ClearBuf();
			}
		}
		bDataBuf.resetReaderIndex();
		return bDecompile;
	}
	private boolean DecompileByte(short value) {
		switch (value) {
			case 126:
				this.ClearBuf();
				break;
			case 127:
				this._Translate = true;
				break;
			default:
				if (this._Translate) {
					// byte value = 0;
					System.err.println("特别需要注意的地方");
					switch (value) {
						case 1:
							value = 126;
							break;
						case 2:
							value = 127;
							break;
						default:
							this.ClearBuf();
							return false;
					}
					this._Translate = false;
					return this.DecompileStep(value);
				}
				return this.DecompileStep(value);
		}
		return false;
	}
	private void push(short value) {
		this._Checksum += value;
		this._Buf.writeByte(value);
	}
	private boolean DecompileStep(short value) {
		if (this._Packet == null) {
			this.ClearBuf();
		}
		switch (this._Step) {
			case 0:
				if (this._Buf.readableBytes() != 4) {
					this.push(value);
					if (this._Buf.readableBytes() == 4) {
						this._Packet.SetCode(this._Buf.readUnsignedInt());
						this._Buf.clear();
						++this._Step;
					}
				}
				else {
					this.ClearBuf();
				}
				break;
			case 1:
				if (this._Buf.readableBytes() != 16) {
					this.push(value);
					if (this._Buf.readableBytes() == 16) {
						try {
							this._Buf.readBytes(this._SNBytes);
							this._Packet.SetSN(new String(this._SNBytes, "GB2312"));
							this._Buf.clear();
							++this._Step;
						}
						catch (Exception var3) {
							this.ClearBuf();
						}
					}
				}
				else {
					this.ClearBuf();
				}
				break;
			case 2:
				if (this._Buf.readableBytes() != 4) {
					this.push(value);
					if (this._Buf.readableBytes() == 4) {
						this._Packet.SetPassword(this._Buf.readUnsignedInt());
						this._Buf.clear();
						++this._Step;
					}
				}
				else {
					this.ClearBuf();
				}
				break;
			case 3:
				this._Checksum += value;
				this._Packet.SetCmdType(value);
				++this._Step;
				break;
			case 4:
				this._Checksum += value;
				this._Packet.SetCmdIndex(value);
				++this._Step;
				break;
			case 5:
				this._Checksum += value;
				this._Packet.SetCmdPar(value);
				++this._Step;
				break;
			case 6:
				if (this._Buf.readableBytes() != 4) {
					this.push(value);
					if (this._Buf.readableBytes() != 4) {
						break;
					}
					this._DataLen = (int) this._Buf.readUnsignedInt();
					if (this._DataLen <= 2046 && this._DataLen >= 0) {
						this._Packet.SetDataLen(this._DataLen);
						this._Buf.clear();
						++this._Step;
						if (this._DataLen != 0) {
							this._DataBuf = DEFAULT_POOLED.buffer(this._DataLen);
							this._Packet.SetDatabuff(this._DataBuf);
						}
						else {
							++this._Step;
						}
						break;
					}
					this.ClearBuf();
					return false;
				}
				this.ClearBuf();
				break;
			case 7:
				if (this._DataBuf.readableBytes() != this._DataLen) {
					this._Checksum += value;
					this._DataBuf.writeByte(value);
					if (this._DataBuf.readableBytes() == this._DataLen) {
						this._Packet.SetDatabuff(this._DataBuf);
						++this._Step;
					}
				}
				else {
					this.ClearBuf();
				}
				break;
			case 8:
				++this._Step;
				this._Checksum &= 255L;
				this._Packet.SetPacketCheck(value);
				return this._Checksum == (long) value;
			default:
				this.ClearBuf();
		}
		return false;
	}
	@Override
	public void ClearBuf() {
		this._Buf.clear();
		this._Step = 0;
		this._Translate = false;
		if (this._Packet != null) {
			this._Packet.Release();
			this._Packet = null;
		}
		this._Packet = new FC8800PacketModel();
		this._Checksum = 0L;
		this._DataBuf = null;
		this._DataLen = 0;
	}
	@Override
	public void Release() {
		if (this._Buf != null) {
			this._Buf.release();
			this._Buf = null;
		}
		this._Step = 0;
		this._Translate = false;
		if (this._Packet != null) {
			this._Packet.Release();
			this._Packet = null;
		}
		this._Checksum = 0L;
		this._DataBuf = null;
		this._DataLen = 0;
	}
}
