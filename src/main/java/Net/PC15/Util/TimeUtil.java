package Net.PC15.Util;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
public class TimeUtil {
	public static Calendar BCDTimeToDate_ssmmhhddMMyy(byte[] btTime) {
		btTime = ByteUtil.BCDToByte(btTime);
		int year = ByteUtil.uByte(btTime[5]);
		int month = ByteUtil.uByte(btTime[4]);
		int dayOfMonth = ByteUtil.uByte(btTime[3]);
		int hourOfDay = ByteUtil.uByte(btTime[2]);
		int minute = ByteUtil.uByte(btTime[1]);
		int second = ByteUtil.uByte(btTime[0]);
		if (year > 99) {
			return null;
		}
		else if (month != 0 && month <= 12) {
			if (dayOfMonth != 0 && dayOfMonth <= 31) {
				if (hourOfDay > 23) {
					return null;
				}
				else if (minute > 59) {
					return null;
				}
				else if (second > 59) {
					return null;
				}
				else {
					Calendar dTime = new GregorianCalendar(2000 + year, month - 1, dayOfMonth, hourOfDay, minute, second);
					return dTime;
				}
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
	}
	public static Calendar BCDTimeToDate_yyMMddhh(byte[] btTime) {
		btTime = ByteUtil.BCDToByte(btTime);
		int year = ByteUtil.uByte(btTime[0]);
		int month = ByteUtil.uByte(btTime[1]);
		int dayOfMonth = ByteUtil.uByte(btTime[2]);
		int hourOfDay = ByteUtil.uByte(btTime[3]);
		if (year > 99) {
			return null;
		}
		else if (month != 0 && month <= 12) {
			if (dayOfMonth != 0 && dayOfMonth <= 31) {
				if (hourOfDay > 23) {
					return null;
				}
				else {
					Calendar dTime = new GregorianCalendar(2000 + year, month - 1, dayOfMonth, hourOfDay, 0, 0);
					return dTime;
				}
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
	}
	public static void DateToBCD_yyMMddhh(byte[] btData, Calendar date) {
		if (date == null) {
			for (int i = 0; i < 4; ++i) {
				btData[i] = 0;
			}
		}
		else {
			btData[0] = (byte) (date.get(1) - 2000);
			btData[1] = (byte) (date.get(2) + 1);
			btData[2] = (byte) date.get(5);
			btData[3] = (byte) date.get(11);
			btData = ByteUtil.ByteToBCD(btData);
		}
	}
	public static void DateToBCD_ssmmhhddMMwwyy(byte[] btData, Calendar date) {
		int i;
		if (date == null) {
			for (i = 0; i < 7; ++i) {
				btData[i] = 0;
			}
		}
		else {
			i = date.get(5) - 1;
			if (i == 0) {
				i = 7;
			}
			btData[6] = (byte) (date.get(1) - 2000);
			btData[5] = (byte) i;
			btData[4] = (byte) (date.get(2) + 1);
			btData[3] = (byte) date.get(5);
			btData[2] = (byte) date.get(11);
			btData[1] = (byte) date.get(12);
			btData[0] = (byte) date.get(13);
			btData = ByteUtil.ByteToBCD(btData);
		}
	}
	public static Calendar BCDTimeToDate_yyMMddhhmm(byte[] btTime) {
		btTime = ByteUtil.BCDToByte(btTime);
		int year = ByteUtil.uByte(btTime[0]);
		int month = ByteUtil.uByte(btTime[1]);
		int dayOfMonth = ByteUtil.uByte(btTime[2]);
		int hourOfDay = ByteUtil.uByte(btTime[3]);
		int minute = ByteUtil.uByte(btTime[4]);
		if (year > 99) {
			return null;
		}
		else if (month != 0 && month <= 12) {
			if (dayOfMonth != 0 && dayOfMonth <= 31) {
				if (hourOfDay > 23) {
					return null;
				}
				else if (minute > 59) {
					return null;
				}
				else {
					Calendar dTime = new GregorianCalendar(2000 + year, month - 1, dayOfMonth, hourOfDay, minute, 0);
					return dTime;
				}
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
	}
	public static void DateToBCD_yyMMddhhmm(byte[] btData, Calendar date) {
		if (date == null) {
			for (int i = 0; i < 5; ++i) {
				btData[i] = 0;
			}
		}
		else {
			btData[0] = (byte) (date.get(1) - 2000);
			btData[1] = (byte) (date.get(2) + 1);
			btData[2] = (byte) date.get(5);
			btData[3] = (byte) date.get(11);
			btData[4] = (byte) date.get(12);
			btData = ByteUtil.ByteToBCD(btData);
		}
	}
	public static Calendar BCDTimeToDate_yyMMddhhmmss(byte[] btTime) {
		btTime = ByteUtil.BCDToByte(btTime);
		int year = ByteUtil.uByte(btTime[0]);
		int month = ByteUtil.uByte(btTime[1]);
		int dayOfMonth = ByteUtil.uByte(btTime[2]);
		int hourOfDay = ByteUtil.uByte(btTime[3]);
		int minute = ByteUtil.uByte(btTime[4]);
		int second = ByteUtil.uByte(btTime[5]);
		if (year > 99) {
			return null;
		}
		else if (month != 0 && month <= 12) {
			if (dayOfMonth != 0 && dayOfMonth <= 31) {
				if (hourOfDay > 23) {
					return null;
				}
				else if (minute > 59) {
					return null;
				}
				else if (second > 59) {
					return null;
				}
				else {
					Calendar dTime = new GregorianCalendar(2000 + year, month - 1, dayOfMonth, hourOfDay, minute, second);
					return dTime;
				}
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
	}
	public static void DateToBCD_yyMMddhhmmss(byte[] btData, Calendar date) {
		if (date == null) {
			for (int i = 0; i < 6; ++i) {
				btData[i] = 0;
			}
		}
		else {
			btData[0] = (byte) (date.get(1) - 2000);
			btData[1] = (byte) (date.get(2) + 1);
			btData[2] = (byte) date.get(5);
			btData[3] = (byte) date.get(11);
			btData[4] = (byte) date.get(12);
			btData[5] = (byte) date.get(13);
			btData = ByteUtil.ByteToBCD(btData);
		}
	}
	public static String FormatTime(Calendar date) {
		if (date == null) {
			return "";
		}
		else {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return format.format(date.getTime());
		}
	}
	public static String FormatTimeHHmm(Calendar date) {
		if (date == null) {
			return "";
		}
		else {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			return format.format(date.getTime());
		}
	}
	public static String FormatTimeMillisecond(Calendar date) {
		if (date == null) {
			return "";
		}
		else {
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.sss");
			return format.format(date.getTime());
		}
	}
}
