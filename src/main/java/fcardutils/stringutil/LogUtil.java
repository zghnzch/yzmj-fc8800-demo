package fcardutils.stringutil;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
/**
 * @author Administrator
 */
public class LogUtil {
	public final static String T = "	";
	public final static String S = " ";
	static Map<Integer, Integer> map = new HashMap<>();
	// static {
	// Calendar calendar = Calendar.getInstance();
	// map.put(0, calendar.get(Calendar.YEAR));// 年
	// map.put(1, calendar.get(Calendar.MONTH) + 1);// 月
	// map.put(2, calendar.get(Calendar.DAY_OF_MONTH));// 日
	// map.put(3, calendar.get(Calendar.HOUR_OF_DAY));// 时
	// map.put(4, calendar.get(Calendar.MINUTE));// 分
	// map.put(5, calendar.get(Calendar.SECOND));// 秒
	// map.put(6, calendar.get(Calendar.DAY_OF_WEEK));// 周几 0~6
	// map.put(7, calendar.get(Calendar.DAY_OF_YEAR));// 本年第N天
	// map.put(8, calendar.get(Calendar.DAY_OF_MONTH));// 本月第N天
	// }
	//获取年月日时分秒等
	public static Integer getT(int t) {
		Calendar calendar = Calendar.getInstance();
		map.put(0, calendar.get(Calendar.YEAR));// 年
		map.put(1, calendar.get(Calendar.MONTH) + 1);// 月
		map.put(2, calendar.get(Calendar.DAY_OF_MONTH));// 日
		map.put(3, calendar.get(Calendar.HOUR_OF_DAY));// 时
		map.put(4, calendar.get(Calendar.MINUTE));// 分
		map.put(5, calendar.get(Calendar.SECOND));// 秒
		map.put(6, calendar.get(Calendar.DAY_OF_WEEK));// 周几  0~6
		map.put(7, calendar.get(Calendar.DAY_OF_YEAR));// 本年第N天
		map.put(8, calendar.get(Calendar.DAY_OF_MONTH));// 本月第N天
		int i = -1;
		if (map.containsKey(t)) {
			i = map.get(t);
		}
		return i;
	}
	public static String bytes2HexString(@NotNull byte[] b) {
		StringBuilder ret = new StringBuilder();
		for (byte value : b) {
			String hex = Integer.toHexString(value & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret.append(hex.toUpperCase());
		}
		return ret.toString();
	}
	/**
	 * convertByteBufToString
	 * @param buf buf
	 * @return String
	 */
	public static String convertByteBufToString(@NotNull ByteBuf buf) {
		String str;
		// 处理堆缓冲区
		if (buf.hasArray()) {
			str = new String(buf.array(), buf.arrayOffset() + buf.readerIndex(), buf.readableBytes());
		}
		// 处理直接缓冲区以及复合缓冲区
		else {
			byte[] bytes = new byte[buf.readableBytes()];
			buf.getBytes(buf.readerIndex(), bytes);
			str = new String(bytes, 0, buf.readableBytes());
		}
		return str;
	}
	public static byte[] getBytesFromByteBuf(ByteBuf buf) {
		byte[] bytes;
		int offset;
		int length = buf.readableBytes();
		if (buf.hasArray()) {
			bytes = buf.array();
			offset = buf.arrayOffset();
		}
		else {
			bytes = new byte[length];
			buf.getBytes(buf.readerIndex(), bytes);
			offset = 0;
		}
		return bytes;
	}
	public static byte[] getBytesFromByteBuf2(ByteBuf buf) {
		byte[] bytes = new byte[buf.readableBytes()];
		int readerIndex = buf.readerIndex();
		buf.getBytes(readerIndex, bytes);
		return bytes;
	}
}
