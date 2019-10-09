package Net.PC15.Connector.TCPServer;
import Net.PC15.Connector.ConnectorDetail;
import Net.PC15.Connector.E_ConnectorType;
public class TCPServerClientDetail extends ConnectorDetail {
	public int ClientID;
	public IPEndPoint Local;
	public IPEndPoint Remote;
	public TCPServerClientDetail(int id) {
		this.ClientID = id;
	}
	public E_ConnectorType GetConnectorType() {
		return E_ConnectorType.OnTCPServer_Client;
	}
	public TCPServerClientDetail clone() throws CloneNotSupportedException {
		TCPServerClientDetail c = (TCPServerClientDetail) super.clone();
		c.ClientID = this.ClientID;
		return c;
	}
}
