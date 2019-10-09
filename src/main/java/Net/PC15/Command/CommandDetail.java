package Net.PC15.Command;
import Net.PC15.Connector.ConnectorDetail;
import Net.PC15.Connector.INConnectorEvent;

import java.util.Calendar;
/**
 * @author zch
 */
public class CommandDetail {
	public ConnectorDetail Connector;
	public INIdentity Identity;
	public String Desc;
	public int ID;
	public String Name;
	public Object UserPar;
	public Calendar EndTime;
	public Calendar BeginTime;
	public INConnectorEvent Event;
	public int Timeout = 500;
	public int RestartCount = 3;
	public CommandDetail() {
		this.init();
	}
	@Override
	public String toString() {
		return "CommandDetail{" + "Connector=" + Connector + ", Identity=" + Identity + ", Desc='" + Desc + '\'' + ", ID=" + ID + ", Name='" + Name + '\'' + ", UserPar=" + UserPar + ", EndTime=" + EndTime + ", BeginTime=" + BeginTime + ", Event=" + Event + ", Timeout=" + Timeout + ", RestartCount=" + RestartCount + '}';
	}
	private void init() {
		this.Identity = null;
		this.Connector = null;
		this.Event = null;
		this.EndTime = null;
		this.BeginTime = null;
		this.UserPar = null;
		this.Desc = null;
		this.Name = null;
	}
	public void release() {
		this.init();
	}
}
