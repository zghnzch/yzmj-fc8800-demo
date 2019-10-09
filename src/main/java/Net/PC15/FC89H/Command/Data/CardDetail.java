//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package Net.PC15.FC89H.Command.Data;
import Net.PC15.Util.ByteUtil;
import Net.PC15.Util.StringUtil;
import Net.PC15.Util.TimeUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import org.apache.log4j.Logger;

import java.math.BigInteger;
import java.util.ArrayList;
public class CardDetail extends Net.PC15.FC8800.Command.Data.CardDetail {
	private final static Logger myLog = Logger.getRootLogger();
	public CardDetail() {
		this.Password = null;
		this.Expiry = null;
		this.TimeGroup = new byte[4];
		this.Door = 0;
		this.Privilege = 0;
		this.CardStatus = 0;
		this.Holiday = new byte[]{-1, -1, -1, -1};
		this.RecordTime = null;
		this.EnterStatus = 0;
		this.HolidayUse = false;
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
	@Override
	public boolean equals(Object o) {
		if (o instanceof CardDetail) {
			return this.compareTo((CardDetail) o) == 0;
		}
		else {
			return false;
		}
	}
	@Override
	public int GetDataLen() {
		return 37;
	}
	@Override
	public String GetCardData() {
		return this.CardData;
	}
	public void SetCardDataHEX(String value) throws Exception {
		if (value != null && value.length() != 0) {
			if (!StringUtil.CanParseInt(value)) {
				System.out.println("ERROR! 卡号不是数字格式!");
				throw new Exception("卡号不是数字格式");
			}
			else {
				String maxHex = (new BigInteger(value, 10)).toString(16);
				BigInteger biLongMax = new BigInteger("18446744073709551615");
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
	@Override
	public void WriteCardData(ByteBuf data) throws Exception {
		data.writeByte(0);
		if (this.CardData != null && this.CardData.length() != 0) {
			if (!StringUtil.CanParseInt(this.CardData)) {
				System.out.println("ERROR! 卡号不是数字格式!");
				throw new Exception("卡号不是数字格式");
			}
			else {
				BigInteger biLongMax = new BigInteger("18446744073709551615");
				myLog.info("CardData:"+CardData);
				// TODO
				BigInteger biCardData = new BigInteger(this.CardData);
				myLog.info("biCardData:"+biCardData);
				if (biLongMax.compareTo(biCardData) <= 0) {
					System.out.println("ERROR! 卡号超过最大值!");
					throw new Exception("卡号超过最大值");
				}
				else {
					String CardDataHex = (new BigInteger(this.CardData, 10)).toString(16);
					CardDataHex = StringUtil.FillString(CardDataHex, 16, "0", false);
					myLog.info("CardDataHex:"+CardDataHex);
					StringUtil.HextoByteBuf(CardDataHex, data);
				}
			}
		}
		else {
			System.out.println("ERROR! 卡号不能为空!");
			throw new Exception("卡号不能为空");
		}
	}
	@Override
	public void ReadCardData(ByteBuf data) {
		data.readByte();
		byte[] btCardData = new byte[8];
		data.readBytes(btCardData, 0, 8);
		this.CardData = ByteUtil.ByteToHex(btCardData);
		this.CardData = StringUtil.LTrim(this.CardData, '0');
		this.CardData = StringUtil.HexStr2Str(this.CardData, 16);
	}
	@Override
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
			System.err.println("特别需要注意的地方");
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
	@Override
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
		myLog.info("调试卡号data:"+ByteBufUtil.hexDump(data).toUpperCase());
	}
}
