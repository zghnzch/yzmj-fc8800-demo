package Net.PC15.FC8800.Command.System.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
public class SearchEquptOnNetNum_Parameter extends CommandParameter {
	public int NetNum;
	public SearchEquptOnNetNum_Parameter(CommandDetail detail, int num) {
		super(detail);
		this.NetNum = num;
	}
}
