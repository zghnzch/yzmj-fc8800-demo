package Net.PC15.Connector;
public class FileDetail extends ConnectorDetail {
	public String File;
	public FileDetail(String fileString) {
		this.File = fileString;
	}
	public E_ConnectorType GetConnectorType() {
		return E_ConnectorType.OnFile;
	}
}
