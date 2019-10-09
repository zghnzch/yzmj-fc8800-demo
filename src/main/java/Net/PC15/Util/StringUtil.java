package Net.PC15.Util;
// import com.sun.xml.internal.bind.v2.TODO;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import org.apache.log4j.Logger;

import java.util.Random;
public class StringUtil {
	private static final byte[] HexToByte_Digit;
	private static final byte[] NumDigit;
	private final static Logger myLog = Logger.getRootLogger();
	static {
		byte[] digits = new byte[256];
		byte[] tmp = "0123456789abcdef".getBytes();
		int i;
		for (i = 0; i < tmp.length; ++i) {
			digits[tmp[i]] = (byte) i;
		}
		tmp = "ABCDEF".getBytes();
		for (i = 0; i < tmp.length; ++i) {
			digits[tmp[i]] = (byte) (i + 10);
		}
		HexToByte_Digit = digits;
		digits = new byte[256];
		tmp = "0123456789".getBytes();
		for (i = 0; i < tmp.length; ++i) {
			digits[tmp[i]] = (byte) i;
		}
		NumDigit = digits;
	}
	public static byte[] HexToByte(String hexString) {
		int iIndex = 0;
		if (IsNullOrEmpty(hexString)) {
			return null;
		}
		else if (!IsHex(hexString)) {
			return null;
		}
		else {
			if (hexString.length() % 2 == 1) {
				hexString = "0" + hexString;
			}
			byte[] digits = HexToByte_Digit;
			byte[] buf = new byte[hexString.length() / 2];
			byte[] sbuf = hexString.getBytes();
			int ilen = sbuf.length;
			for (int i = 0; i < ilen; ++i) {
				int iData = digits[sbuf[i++] & 255] * 16;
				iData += digits[sbuf[i]];
				buf[iIndex] = (byte) iData;
				++iIndex;
			}
			return buf;
		}
	}
	public static void HextoByteBuf(String hexString, ByteBuf buf) {
		int iIndex = 0;
		if (!IsNullOrEmpty(hexString)) {
			if (IsHex(hexString)) {
				if (hexString.length() % 2 == 1) {
					hexString = "0" + hexString;
				}
				myLog.info("发送过程中的hexString："+hexString);
				byte[] digits = HexToByte_Digit;
				byte[] sbuf = hexString.getBytes();
				// TODO
				int ilen = sbuf.length;
				for (int i = 0; i < ilen; ++i) {
					int iData = digits[sbuf[i++] & 255] * 16;
					iData += digits[sbuf[i]];
					buf.writeByte((byte) iData);
					++iIndex;
				}
				myLog.info("发送过程中的hexString2："+hexString);
				myLog.info("发送过程中的hexString3digits："+ByteBufUtil.hexDump(digits).toUpperCase());
				myLog.info("发送过程中的hexString4sbuf："+ByteBufUtil.hexDump(sbuf).toUpperCase());
				myLog.info("发送过程中的hexString5buf："+ByteBufUtil.hexDump(buf).toUpperCase());
			}
		}
	}
	public static byte[] HextoByte(String hexString) {
		int iIndex = 0;
		if (IsNullOrEmpty(hexString)) {
			return null;
		}
		else if (!IsHex(hexString)) {
			return null;
		}
		else {
			if (hexString.length() % 2 == 1) {
				hexString = "0" + hexString;
			}
			byte[] digits = HexToByte_Digit;
			byte[] sbuf = hexString.getBytes();
			int ilen = sbuf.length;
			byte[] b = new byte[sbuf.length];
			for (int i = 0; i < ilen; ++i) {
				int iData = digits[sbuf[i++] & 255] * 16;
				iData += digits[sbuf[i]];
				b[i] = (byte) iData;
				++iIndex;
			}
			return b;
		}
	}
	public static boolean IsHex(String hexString) {
		if (IsNullOrEmpty(hexString)) {
			return false;
		}
		else {
			byte[] sbuf = hexString.getBytes();
			byte[] digits = HexToByte_Digit;
			int ilen = sbuf.length;
			for (int i = 0; i < ilen; ++i) {
				if (digits[sbuf[i] & 255] == 0 && sbuf[i] != 48) {
					return false;
				}
			}
			return true;
		}
	}
	public static boolean IsNum(String numString) {
		if (IsNullOrEmpty(numString)) {
			return false;
		}
		else {
			byte[] sbuf = numString.getBytes();
			byte[] digits = NumDigit;
			int ilen = sbuf.length;
			for (int i = 0; i < ilen; ++i) {
				if (digits[sbuf[i] & 255] == 0 && sbuf[i] != 48) {
					return false;
				}
			}
			return true;
		}
	}
	public static boolean IsNullOrEmpty(String str) {
		if (str == null) {
			return true;
		}
		else {
			return str.length() == 0;
		}
	}
	public static boolean IsAscii(String asciiString) {
		if (IsNullOrEmpty(asciiString)) {
			return false;
		}
		else {
			byte[] buf = asciiString.getBytes();
			int ilen = buf.length;
			for (int i = 0; i < ilen; ++i) {
				if (buf[i] < 32 || buf[i] > 125) {
					return false;
				}
			}
			return true;
		}
	}
	public static String FillString(String str, int iLen, String fillstr) {
		return FillString(str, iLen, fillstr, true);
	}
	public static String FillString(String str, int iLen, String fillstr, boolean fill_right) {
		int iStrLen = 0;
		if (!IsNullOrEmpty(str)) {
			iStrLen = str.length();
			if (iStrLen == iLen) {
				return str;
			}
		}
		if (iStrLen > iLen) {
			return fill_right ? str.substring(0, iLen) : str.substring(iStrLen - iLen, iStrLen);
		}
		else {
			StringBuilder sbuf = new StringBuilder(iLen);
			int iAddCount = iLen - iStrLen;
			for (int i = 0; i < iAddCount; ++i) {
				sbuf.append(fillstr);
				if (sbuf.length() > iAddCount) {
					break;
				}
			}
			if (sbuf.length() > iAddCount) {
				sbuf.setLength(iAddCount);
			}
			if (!IsNullOrEmpty(str)) {
				if (fill_right) {
					sbuf.insert(0, str);
				}
				else {
					sbuf.append(str);
				}
			}
			return sbuf.toString();
		}
	}
	public static String FillHexString(String str, int iLen, String fillstr, boolean fill_right) {
		StringBuilder sbuf = new StringBuilder(iLen);
		int i;
		if (IsNullOrEmpty(str)) {
			for (i = 0; i < iLen; ++i) {
				sbuf.append(fillstr);
			}
			sbuf.setLength(iLen);
			return sbuf.toString();
		}
		else if (IsHex(str)) {
			return FillString(str, iLen, fillstr, fill_right);
		}
		else {
			for (i = 0; i < iLen; ++i) {
				sbuf.append(fillstr);
			}
			sbuf.setLength(iLen);
			return sbuf.toString();
		}
	}
	public static String[] LongToString(long[] longArray) {
		if (longArray != null && longArray.length >= 1) {
			String[] stringArray = new String[longArray.length];
			for (int i = 0; i < stringArray.length; ++i) {
				try {
					stringArray[i] = String.valueOf(longArray[i]);
				}
				catch (NumberFormatException var4) {
					stringArray[i] = null;
				}
			}
			return stringArray;
		}
		else {
			return null;
		}
	}
	public static String HexStr2Str(String hexStr, int ii) {
		for (int i = 0; i < hexStr.length(); ++i) {
			char cc = hexStr.charAt(i);
			if (cc != '0' && cc != '1' && cc != '2' && cc != '3' && cc != '4' && cc != '5' && cc != '6' && cc != '7' && cc != '8' && cc != '9' && cc != 'A' && cc != 'B' && cc != 'C' && cc != 'D' && cc != 'E' && cc != 'F' && cc != 'a' && cc != 'b' && cc != 'c' && cc != 'c' && cc != 'd' && cc != 'e' && cc != 'f') {
				return hexStr;
			}
		}
		Integer x = Integer.parseInt(hexStr, ii);
		return String.valueOf(x);
	}
	public static boolean CanParseHex(String hexStr, int ii) {
		for (int i = 0; i < hexStr.length(); ++i) {
			char cc = hexStr.charAt(i);
			if (cc != '0' && cc != '1' && cc != '2' && cc != '3' && cc != '4' && cc != '5' && cc != '6' && cc != '7' && cc != '8' && cc != '9' && cc != 'A' && cc != 'B' && cc != 'C' && cc != 'D' && cc != 'E' && cc != 'F' && cc != 'a' && cc != 'b' && cc != 'c' && cc != 'c' && cc != 'd' && cc != 'e' && cc != 'f') {
				return false;
			}
		}
		return true;
	}
	public static boolean CanParseInt(String str) {
		return str != null && str.matches("\\d+");
	}
	public static String StrTo16(String s) {
		String str = "";
		for (int i = 0; i < s.length(); ++i) {
			int ch = s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str = str + s4;
		}
		return str;
	}
	public static String bytesToHexFun2(byte[] bytes) {
		char[] HEX_CHAR = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		char[] buf = new char[bytes.length * 2];
		int index = 0;
		byte[] var4 = bytes;
		int var5 = bytes.length;
		for (int var6 = 0; var6 < var5; ++var6) {
			byte b = var4[var6];
			buf[index++] = HEX_CHAR[b >>> 4 & 15];
			buf[index++] = HEX_CHAR[b & 15];
		}
		return new String(buf);
	}
	public static String randomHexString(int len) {
		try {
			StringBuffer result = new StringBuffer();
			for (int i = 0; i < len; ++i) {
				result.append(Integer.toHexString((new Random()).nextInt(16)));
			}
			return result.toString().toUpperCase();
		}
		catch (Exception var3) {
			var3.printStackTrace();
			return null;
		}
	}
	public static String LTrim(String strArg, char text) {
		char[] cVal = strArg.toCharArray();
		int p1 = 0;
		int len;
		for (len = cVal.length; p1 < len && cVal[p1] == text; ++p1) {
		}
		if (p1 == len) {
			return "";
		}
		else {
			int p2 = len - 1;
			String subStr = strArg.substring(p1, p2 + 1);
			return subStr;
		}
	}
	public static String str2HexStr(String str) {
		char[] chars = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder();
		byte[] bs = str.getBytes();
		for (int i = 0; i < bs.length; ++i) {
			int bit = (bs[i] & 240) >> 4;
			sb.append(chars[bit]);
			bit = bs[i] & 15;
			sb.append(chars[bit]);
		}
		return sb.toString().trim();
	}
}
