package Net.PC15.Connector;
import Net.PC15.Command.INCommand;
import Net.PC15.Command.INCommandResult;
import Net.PC15.Connector.TCPServer.TCPServerClientDetail;
import Net.PC15.Data.INData;
public interface INConnectorEvent {
	void CommandCompleteEvent(INCommand var1, INCommandResult var2);
	void CommandProcessEvent(INCommand var1);
	void ConnectorErrorEvent(INCommand var1, boolean var2);
	void ConnectorErrorEvent(ConnectorDetail var1);
	void CommandTimeout(INCommand var1);
	void PasswordErrorEvent(INCommand var1);
	void ChecksumErrorEvent(INCommand var1);
	void WatchEvent(ConnectorDetail var1, INData var2);
	void ClientOnline(TCPServerClientDetail var1);
	void ClientOffline(TCPServerClientDetail var1);
}
