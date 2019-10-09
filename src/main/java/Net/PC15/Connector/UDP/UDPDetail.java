package Net.PC15.Connector.UDP;
import Net.PC15.Connector.ConnectorDetail;
import Net.PC15.Connector.E_ConnectorType;
public class UDPDetail extends ConnectorDetail {
	public String IP;
	public int Port;
	public String LocalIP;
	public int LocalPort;
	public boolean Broadcast;
	public UDPDetail(String ip, int port) {
		this.IP = ip;
		this.Port = port;
	}
	public E_ConnectorType GetConnectorType() {
		return E_ConnectorType.OnUDP;
	}
	public UDPDetail clone() throws CloneNotSupportedException {
		UDPDetail c = (UDPDetail) super.clone();
		c.IP = this.IP;
		c.Port = this.Port;
		c.Broadcast = this.Broadcast;
		c.LocalIP = this.LocalIP;
		c.LocalPort = this.LocalPort;
		return c;
	}
}
