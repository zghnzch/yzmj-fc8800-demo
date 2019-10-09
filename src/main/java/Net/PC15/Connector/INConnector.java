package Net.PC15.Connector;
import Net.PC15.Command.INCommand;
import Net.PC15.Command.INIdentity;
import Net.PC15.Command.INWatchResponse;
public interface INConnector extends Runnable {
	E_ConnectorType GetConnectorType();
	int GetCommandCount();
	void AddCommand(INCommand var1);
	void AddWatchDecompile(INWatchResponse var1);
	void Release();
	boolean IsForciblyConnect();
	void OpenForciblyConnect();
	void CloseForciblyConnect();
	boolean IsInvalid();
	void SetEventHandle(INConnectorEvent var1);
	E_ConnectorStatus GetStatus();
	void StopCommand(INIdentity var1);
	void run();
	boolean TaskIsBegin();
	void SetTaskIsBegin();
}
