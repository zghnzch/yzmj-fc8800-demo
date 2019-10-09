package Net.PC15.Util;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
public class ByteUtil {
	public static final ByteBufAllocator UNPOOLED_HEAP_ALLOCATOR = new UnpooledByteBufAllocator(false);
	public static final ByteBufAllocator ALLOCATOR;
	public static final short UINT8_MAX = 255;
	public static final long UINT8_MIn = 0L;
	private static final char[] ByteToHex_Digit;
	static {
		ALLOCATOR = UNPOOLED_HEAP_ALLOCATOR;
		ByteToHex_Digit = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
	}
	public static boolean Check(short num) {
		return num <= 255 && (long) num >= 0L;
	}
	public static int uByte(byte byte0) {
		return byte0 & 255;
	}
	public static String ByteToHex(byte[] b) {
		if (b == null) {
			return null;
		}
		else if (b.length == 0) {
			return null;
		}
		else {
			int ilen = b.length;
			char[] sHexbuf = new char[ilen * 2];
			int lIndex = 0;
			char[] digits = ByteToHex_Digit;
			try {
				for (int i = 0; i < ilen; ++i) {
					int iData = uByte(b[i]);
					sHexbuf[lIndex++] = digits[iData / 16];
					sHexbuf[lIndex++] = digits[iData % 16];
				}
				return (new String(sHexbuf)).toUpperCase();
			}
			catch (Exception var7) {
				return "";
			}
		}
	}
	public static String BytesToString(byte[] bBytes) {
		if (bBytes != null && bBytes.length != 0) {
			int ilen = bBytes.length;
			StringBuilder buf = new StringBuilder(ilen * 5);
			int iRowCount = 0;
			for (int i = 0; i < bBytes.length; ++i) {
				int ret = uByte(bBytes[i]);
				buf.append(ret);
				buf.append(",");
				++iRowCount;
				if (iRowCount == 8) {
					buf.append("\n");
					iRowCount = 0;
				}
			}
			buf.setLength(buf.length() - 1);
			return buf.toString();
		}
		else {
			return null;
		}
	}
	public static byte BoolToByte(boolean b) {
		return (byte) (b ? 1 : 0);
	}
	public static byte BCDToByte(byte iNum) {
		int iValue = uByte(iNum);
		iValue = iValue / 16 * 10 + iValue % 16;
		return (byte) iValue;
	}
	public static byte[] BCDToByte(byte[] iNum) {
		int iLen = iNum.length;
		for (int i = 0; i < iLen; ++i) {
			iNum[i] = BCDToByte(iNum[i]);
		}
		return iNum;
	}
	public static byte ByteToBCD(byte iNum) {
		int iValue = uByte(iNum);
		iValue = iValue / 10 * 16 + iValue % 10;
		return (byte) iValue;
	}
	public static byte[] ByteToBCD(byte[] iNum) {
		int iLen = iNum.length;
		for (int i = 0; i < iLen; ++i) {
			iNum[i] = ByteToBCD(iNum[i]);
		}
		return iNum;
	}
}
