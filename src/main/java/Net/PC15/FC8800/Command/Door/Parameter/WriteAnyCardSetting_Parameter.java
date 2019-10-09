package Net.PC15.FC8800.Command.Door.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
public class WriteAnyCardSetting_Parameter extends CommandParameter {
	public int DoorNum;
	public boolean Use;
	public boolean AutoSave;
	public int AutoSaveTimeGroupIndex;
	public WriteAnyCardSetting_Parameter(CommandDetail detail, int door, boolean use, boolean AutoSave, int AutoSaveTimeGroupIndex) {
		super(detail);
		this.DoorNum = door;
		this.Use = use;
		this.AutoSave = AutoSave;
		this.AutoSaveTimeGroupIndex = AutoSaveTimeGroupIndex;
	}
}
