package Net.PC15.FC8800.Command.Door.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.Data.TimeGroup.WeekTimeGroup;
public class WritePushButtonSetting_Parameter extends CommandParameter {
	public int DoorNum;
	public boolean Use;
	public boolean NormallyOpen;
	public WeekTimeGroup TimeGroup;
	public WritePushButtonSetting_Parameter(CommandDetail detail, int door, boolean use, boolean normallyOpen) {
		super(detail);
		this.DoorNum = door;
		this.Use = use;
		this.NormallyOpen = normallyOpen;
		this.TimeGroup = new WeekTimeGroup(8);
	}
}
