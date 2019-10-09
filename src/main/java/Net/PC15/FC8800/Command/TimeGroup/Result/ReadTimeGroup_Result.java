package Net.PC15.FC8800.Command.TimeGroup.Result;
import Net.PC15.Command.INCommandResult;

import java.util.ArrayList;
public class ReadTimeGroup_Result implements INCommandResult {
	public ArrayList List;
	public int DataBaseSize;
	public void release() {
		this.List = null;
	}
}
