package Net.PC15.Connector.TCPServer;
public class TCPServer_ClientDetail extends IPEndPoint {
	private int _ClientID;
	public TCPServer_ClientDetail(String ip, int port, int client) {
		super(ip, port);
		this._ClientID = client;
	}
	public int ClientID() {
		return this._ClientID;
	}
}
