package Net.PC15.FC8800.Command.TimeGroup.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;

import java.util.ArrayList;
public class AddTimeGroup_Parameter extends CommandParameter {
	public ArrayList List;
	public AddTimeGroup_Parameter(CommandDetail detail, ArrayList list) {
		super(detail);
		this.List = list;
	}
}
