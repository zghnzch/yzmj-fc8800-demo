package Net.PC15.Connector;
public abstract class ConnectorDetail implements Cloneable {
	public int Timeout = 5000;
	public int RestartCount = 2;
	public abstract E_ConnectorType GetConnectorType();
	public ConnectorDetail clone() throws CloneNotSupportedException {
		ConnectorDetail c = (ConnectorDetail) super.clone();
		c.RestartCount = this.RestartCount;
		c.Timeout = this.Timeout;
		return c;
	}
}
