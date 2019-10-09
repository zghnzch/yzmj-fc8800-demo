package Net.PC15.FC8800.Command.System.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Util.StringUtil;
public class WriteSN_Parameter extends CommandParameter {
	private String _SN;
	public WriteSN_Parameter(CommandDetail detail, String SN) {
		super(detail);
		this.SetSN(SN);
	}
	public String GetSN() {
		return this._SN;
	}
	public void SetSN(String sn) {
		if (!StringUtil.IsNullOrEmpty(sn)) {
			if (StringUtil.IsAscii(sn)) {
				if (sn.length() == 16) {
					this._SN = sn;
				}
			}
		}
	}
}
