package Net.PC15.Connector.TCPServer;
public class IPEndPoint {
	private String _IP;
	private int _Port;
	public IPEndPoint(String ip, int port) {
		this._IP = ip;
		this._Port = port;
	}
	public String IP() {
		return this._IP;
	}
	public int Port() {
		return this._Port;
	}
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this._IP);
		sb.append(":");
		sb.append(this._Port);
		return sb.toString();
	}
}
