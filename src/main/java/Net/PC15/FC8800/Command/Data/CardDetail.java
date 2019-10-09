//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package Net.PC15.FC8800.Command.Data;
import Net.PC15.Data.INData;
import Net.PC15.Util.ByteUtil;
import Net.PC15.Util.StringUtil;
import Net.PC15.Util.TimeUtil;
import io.netty.buffer.ByteBuf;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
public class CardDetail implements INData, Comparable<CardDetail> {
	public String Password = null;
	public Calendar Expiry = null;
	public int OpenTimes;
	public byte CardStatus = 0;
	public boolean HolidayUse = false;
	public int EnterStatus = 0;
	public Calendar RecordTime = null;
	protected String CardData = null;
	protected byte[] TimeGroup = new byte[4];
	protected int Door = 0;
	protected int Privilege = 0;
	protected byte[] Holiday = new byte[]{-1, -1, -1, -1};
	public CardDetail() {
	}
	public static int SearchCardDetail(ArrayList<CardDetail> list, String SearchCard) {
		CardDetail search = new CardDetail();
		search.CardData = SearchCard;
		return SearchCardDetail(list, search);
	}
	public static int SearchCardDetail(ArrayList<CardDetail> list, CardDetail search) {
		int max = list.size() - 1;
		int min = 0;
		while (min <= max) {
			int mid = max + min >> 1;
			CardDetail cd = list.get(mid);
			int num = cd.compareTo(search);
			if (num > 0) {
				max = mid - 1;
			}
			else {
				if (num >= 0) {
					return mid;
				}
				min = mid + 1;
			}
		}
		return -1;
	}
	public int compareTo(CardDetail o) {
		return o.CardData.equals(this.CardData) ? 0 : -1;
	}
	public boolean equals(Object o) {
		if (o instanceof CardDetail) {
			return this.compareTo((CardDetail) o) == 0;
		}
		else {
			return false;
		}
	}
	public int GetDataLen() {
		return 33;
	}
	public void SetCardData(String value) throws Exception {
		if (value != null && value.length() != 0) {
			if (!StringUtil.CanParseInt(value)) {
				System.out.println("ERROR! 卡号不是数字格式!");
				throw new Exception("卡号不是数字格式");
			}
			else {
				BigInteger biLongMax = new BigInteger(String.valueOf(9223372036854775807L));
				BigInteger biCardData = new BigInteger(value);
				if (biLongMax.compareTo(biCardData) <= 0) {
					System.out.println("ERROR! 卡号超过最大值!");
					throw new Exception("卡号超过最大值");
				}
				else {
					this.CardData = value;
				}
			}
		}
		else {
			System.out.println("ERROR! 卡号不能为空!");
			throw new Exception("卡号不能为空");
		}
	}
	public void WriteCardData(ByteBuf data) throws Exception {
		data.writeByte(0);
		if (this.CardData != null && this.CardData.length() != 0) {
			if (!StringUtil.CanParseInt(this.CardData)) {
				System.out.println("ERROR! 卡号不是数字格式!");
				throw new Exception("卡号不是数字格式");
			}
			else {
				BigInteger biLongMax = new BigInteger(String.valueOf(9223372036854775807L));
				BigInteger biCardData = new BigInteger(this.CardData);
				if (biLongMax.compareTo(biCardData) <= 0) {
					System.out.println("ERROR! 卡号超过最大值!");
					throw new Exception("卡号超过最大值");
				}
				else {
					data.writeInt(Integer.valueOf(this.CardData));
				}
			}
		}
		else {
			System.out.println("ERROR! 卡号不能为空!");
			throw new Exception("卡号不能为空");
		}
	}
	public void ReadCardData(ByteBuf data) {
		data.readByte();
		this.CardData = String.valueOf(data.readUnsignedInt());
	}
	public void SetBytes(ByteBuf data) {
		this.ReadCardData(data);
		byte[] btData = new byte[4];
		data.readBytes(btData, 0, 4);
		this.Password = ByteUtil.ByteToHex(btData);
		byte[] btTime = new byte[6];
		data.readBytes(btTime, 0, 5);
		this.Expiry = TimeUtil.BCDTimeToDate_yyMMddhhmm(btTime);
		data.readBytes(btData, 0, 4);
		int bData2;
		for (bData2 = 0; bData2 < 4; ++bData2) {
			this.TimeGroup[bData2] = btData[bData2];
		}
		this.OpenTimes = data.readUnsignedShort();
		int bData = data.readUnsignedByte();
		this.Door = bData >> 4;
		bData = bData & 15;
		this.Privilege = bData & 7;
		this.HolidayUse = (bData & 8) == 8;
		this.CardStatus = data.readByte();
		data.readBytes(btData, 0, 4);
		for (int i = 0; i < 4; ++i) {
			this.Holiday[i] = btData[i];
		}
		this.EnterStatus = data.readByte();
		data.readBytes(btTime, 0, 6);
		this.RecordTime = TimeUtil.BCDTimeToDate_yyMMddhhmmss(btTime);
	}
	public ByteBuf GetBytes() {
		return null;
	}
	public void GetBytes(ByteBuf data) {
		try {
			this.WriteCardData(data);
		}
		catch (Exception var6) {
			return;
		}
		this.Password = StringUtil.FillHexString(this.Password, 8, "F", true);
		long pwd = Long.parseLong(this.Password, 16);
		data.writeInt((int) pwd);
		byte[] btTime = new byte[6];
		TimeUtil.DateToBCD_yyMMddhhmm(btTime, this.Expiry);
		data.writeBytes(btTime, 0, 5);
		data.writeBytes(this.TimeGroup, 0, 4);
		data.writeShort(this.OpenTimes);
		int bData = (this.Door << 4) + this.Privilege;
		if (this.HolidayUse) {
			bData |= 8;
		}
		data.writeByte(bData);
		data.writeByte(this.CardStatus);
		data.writeBytes(this.Holiday, 0, 4);
		data.writeByte(this.EnterStatus);
		TimeUtil.DateToBCD_yyMMddhhmmss(btTime, this.RecordTime);
		data.writeBytes(btTime, 0, 6);
	}
	public String GetCardData() {
		return this.CardData;
	}
	public int GetTimeGroup(int iDoor) {
		if (iDoor >= 0 && iDoor <= 4) {
			return this.TimeGroup[iDoor - 1];
		}
		else {
			throw new IllegalArgumentException("Door 1-4");
		}
	}
	public void SetTimeGroup(int iDoor, int iNum) {
		if (iDoor >= 0 && iDoor <= 4) {
			if (iNum >= 0 && iNum <= 64) {
				this.TimeGroup[iDoor - 1] = (byte) iNum;
			}
			else {
				throw new IllegalArgumentException("Num 1-64");
			}
		}
		else {
			throw new IllegalArgumentException("Door 1-4");
		}
	}
	public boolean GetDoor(int iDoor) {
		if (iDoor >= 0 && iDoor <= 4) {
			--iDoor;
			int iBitIndex = iDoor % 8;
			int iMaskValue = (int) Math.pow(2.0D, iBitIndex);
			int iByteValue = this.Door & iMaskValue;
			if (iBitIndex > 0) {
				iByteValue >>= iBitIndex;
			}
			return iByteValue == 1;
		}
		else {
			throw new IllegalArgumentException("Door 1-4");
		}
	}
	public void SetDoor(int iDoor, boolean bUse) {
		if (iDoor >= 0 && iDoor <= 4) {
			if (bUse != this.GetDoor(iDoor)) {
				--iDoor;
				int iBitIndex = iDoor % 8;
				int iMaskValue = (int) Math.pow(2.0D, iBitIndex);
				if (bUse) {
					this.Door |= iMaskValue;
				}
				else {
					this.Door ^= iMaskValue;
				}
			}
		}
		else {
			throw new IllegalArgumentException("Door 1-4");
		}
	}
	public boolean IsNormal() {
		return this.Privilege == 0;
	}
	public void SetNormal() {
		this.Privilege = 0;
	}
	public boolean IsPrivilege() {
		return this.Privilege == 1;
	}
	public void SetPrivilege() {
		this.Privilege = 1;
	}
	public boolean IsTiming() {
		return this.Privilege == 2;
	}
	public void SetTiming() {
		this.Privilege = 2;
	}
	public boolean IsGuardTour() {
		return this.Privilege == 3;
	}
	public void SetGuardTour() {
		this.Privilege = 3;
	}
	public boolean IsAlarmSetting() {
		return this.Privilege == 4;
	}
	public void SetAlarmSetting() {
		this.Privilege = 4;
	}
	public boolean GetHolidayValue(int iIndex) {
		if (iIndex > 0 && iIndex <= 30) {
			--iIndex;
			int iByteIndex = iIndex / 8;
			int iBitIndex = iIndex % 8;
			int iByteValue = this.Holiday[iByteIndex] & 255;
			int iMaskValue = (int) Math.pow(2.0D, iBitIndex);
			iByteValue &= iMaskValue;
			if (iBitIndex > 0) {
				iByteValue >>= iBitIndex;
			}
			return iByteValue == 1;
		}
		else {
			throw new IllegalArgumentException("iIndex= 1 -- 32");
		}
	}
	public void SetHolidayValue(int iIndex, boolean bUse) {
		if (iIndex > 0 && iIndex <= 30) {
			if (bUse != this.GetHolidayValue(iIndex)) {
				--iIndex;
				int iByteIndex = iIndex / 8;
				int iBitIndex = iIndex % 8;
				int iByteValue = this.Holiday[iByteIndex] & 255;
				int iMaskValue = (int) Math.pow(2.0D, iBitIndex);
				if (bUse) {
					iByteValue |= iMaskValue;
				}
				else {
					iByteValue ^= iMaskValue;
				}
				this.Holiday[iByteIndex] = (byte) iByteValue;
			}
		}
		else {
			throw new IllegalArgumentException("iIndex= 1 -- 32");
		}
	}
}
