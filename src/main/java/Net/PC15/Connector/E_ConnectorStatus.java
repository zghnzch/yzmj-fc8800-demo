package Net.PC15.Connector;
public enum E_ConnectorStatus {
	OnClosed(0), OnConnecting(1), OnConnected(2), OnError(3), OnConnectTimeout(4), OnClosing(5);
	private final int value;
	E_ConnectorStatus(int value) {
		this.value = value;
	}
	public int getValue() {
		return this.value;
	}
}
