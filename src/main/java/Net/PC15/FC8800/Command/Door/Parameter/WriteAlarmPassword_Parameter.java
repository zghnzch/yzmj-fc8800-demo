package Net.PC15.FC8800.Command.Door.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Util.StringUtil;
public class WriteAlarmPassword_Parameter extends CommandParameter {
	public int DoorNum;
	public boolean Use;
	public int AlarmOption;
	protected String Password;
	public WriteAlarmPassword_Parameter(CommandDetail detail, int door, boolean use, String pwd, int alarmOption) {
		super(detail);
		this.DoorNum = door;
		this.Use = use;
		this.SetPassword(pwd);
		this.AlarmOption = alarmOption;
	}
	public String GetPassword() {
		return this.Password;
	}
	public void SetPassword(String pwd) {
		if (StringUtil.IsNullOrEmpty(pwd)) {
			this.Password = null;
		}
		else {
			if (!StringUtil.IsNum(this.Password)) {
				throw new IllegalArgumentException("pwd is not number!");
			}
			if (pwd.length() > 8) {
				throw new IllegalArgumentException("pwd.length() > 8");
			}
			this.Password = pwd;
		}
	}
}
