package Net.PC15.FC8800.Command.System.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Util.StringUtil;
public class WriteConnectPassword_Parameter extends CommandParameter {
	private String _PWD;
	public WriteConnectPassword_Parameter(CommandDetail detail, String pwd) {
		super(detail);
		this.SetPassword(pwd);
	}
	public String GetPassword() {
		return this._PWD;
	}
	public void SetPassword(String password) {
		if (!StringUtil.IsNullOrEmpty(password)) {
			if (StringUtil.IsHex(password)) {
				if (password.length() == 8) {
					this._PWD = password;
				}
			}
		}
	}
}
