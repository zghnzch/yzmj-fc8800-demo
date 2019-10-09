package Net.PC15.Command;
public class CommandParameter implements INCommandParameter {
	protected CommandDetail _Detail;
	public CommandParameter(CommandDetail detail) {
		this._Detail = detail;
	}
	@Override
	public CommandDetail getCommandDetail() {
		return this._Detail;
	}
	@Override
	public void setCommandDetail(CommandDetail detail) {
		this._Detail = detail;
	}
}
