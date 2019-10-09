package Net.PC15.Connector;
public enum E_ConnectorType {
	OnComm(0), OnTCPClient(1), OnTCPServer_Client(2), OnUDP(3), OnFile(4);
	private final int value;
	E_ConnectorType(int value) {
		this.value = value;
	}
	public int getValue() {
		return this.value;
	}
}
