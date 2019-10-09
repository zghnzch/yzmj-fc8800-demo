package Net.PC15.FC8800;
import Net.PC15.Command.INIdentity;
import Net.PC15.Connector.E_ControllerType;
import Net.PC15.Util.StringUtil;
public class FC8800Identity implements INIdentity {
	private String mPassword;
	private String mSN;
	private E_ControllerType mType;
	public FC8800Identity(String sn, String pwd, E_ControllerType type) {
		this.SetSN(sn);
		this.SetPassword(pwd);
		this.SetType(type);
	}
	@Override
	public String GetPassword() {
		return this.mPassword;
	}
	@Override
	public String GetIdentity() {
		return this.mSN;
	}
	@Override
	public E_ControllerType GetIdentityType() {
		return this.mType;
	}
	public void SetType(E_ControllerType type) {
		this.mType = type;
	}
	public void SetPassword(String pwd) {
		if (!StringUtil.IsNullOrEmpty(pwd) && StringUtil.IsHex(pwd) && pwd.length() <= 16 && pwd.length() >= 8) {
			this.mPassword = pwd;
		}
		else {
			throw new IllegalArgumentException("pwd 不是十六进制或长度超出限制.");
		}
	}
	public void SetSN(String sn) {
		if (!StringUtil.IsNullOrEmpty(sn) && StringUtil.IsAscii(sn) && sn.length() == 16) {
			this.mSN = sn;
		}
		else {
			throw new IllegalArgumentException("sn 长度不正确或不是有效的Ascii字符.");
		}
	}
	public String GetSN() {
		return this.mSN;
	}
	@Override
	public boolean equals(INIdentity dec) {
		if (dec == null) {
			return false;
		}
		else {
			String clsName = this.getClass().getCanonicalName();
			String decName = dec.getClass().getCanonicalName();
			return clsName.equals(decName) && this.GetIdentity().equals(dec.GetIdentity());
		}
	}
}
