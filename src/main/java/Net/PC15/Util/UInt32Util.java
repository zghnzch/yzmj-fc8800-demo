package Net.PC15.Util;
import io.netty.buffer.ByteBuf;

import java.util.Random;
public class UInt32Util {
	public static final long UINT32_MAX = 4294967295L;
	public static final long UINT32_MIn = 0L;
	private static final Random DEFAULT_Random = new Random(System.currentTimeMillis());
	public static boolean Check(long num) {
		return num <= 4294967295L && num >= 0L;
	}
	public static long UInt32(int num) {
		return num & -1;
	}
	public static long GetRndNum() {
		long ivalue = (long) (DEFAULT_Random.nextDouble() * 4.294967295E9D);
		return ivalue + 4096L;
	}
	public static ByteBuf UINT32ToByteBuf(long num) {
		ByteBuf buf = ByteUtil.ALLOCATOR.buffer(4);
		buf.writeInt((int) num);
		return buf;
	}
	public static boolean CheckNum(int Num, int iMin, int iMax) {
		if (Num < iMin) {
			return false;
		}
		else {
			return Num <= iMax;
		}
	}
}
