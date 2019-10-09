package Net.PC15.Connector.TCPClient;
import Net.PC15.Connector.ConnectorDetail;
import Net.PC15.Connector.E_ConnectorType;
/**
 * @author zch
 */
public class TCPClientDetail extends ConnectorDetail {
	public String IP;
	public int Port;
	public TCPClientDetail(String ip, int port) {
		this.IP = ip;
		this.Port = port;
	}
	@Override
	public E_ConnectorType GetConnectorType() {
		return E_ConnectorType.OnTCPClient;
	}
	@Override
	public TCPClientDetail clone() throws CloneNotSupportedException {
		TCPClientDetail c = (TCPClientDetail) super.clone();
		c.IP = this.IP;
		c.Port = this.Port;
		return c;
	}
}
