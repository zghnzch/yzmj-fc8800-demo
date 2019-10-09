package Net.PC15.Command;
import Net.PC15.Connector.E_ControllerType;
public interface INIdentity {
	String GetPassword();
	String GetIdentity();
	E_ControllerType GetIdentityType();
	boolean equals(INIdentity var1);
}
