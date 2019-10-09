package Net.PC15.FC8800.Command.Data;
import Net.PC15.Util.ByteUtil;
public class DoorPortDetail {
	public final short DoorMax;
	public byte[] DoorPort;
	public DoorPortDetail(short iDoorMax) {
		this.DoorMax = iDoorMax;
		this.DoorPort = new byte[iDoorMax];
	}
	public void SetDoor(int iDoor, int iValue) {
		if (iDoor >= 1 || iDoor <= 4) {
			this.DoorPort[iDoor - 1] = (byte) iValue;
		}
	}
	public int GetDoor(int iDoor) {
		return iDoor < 1 && iDoor > 4 ? 0 : ByteUtil.uByte(this.DoorPort[iDoor - 1]);
	}
}
