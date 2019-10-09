package Net.PC15.Command;
/**
 * @author zch
 */
public enum E_CommandStatus {
	// OnReady
	OnReady(0), // OnWaitResponse
	OnWaitResponse(1), // OnOver
	OnOver(2);
	private final int value;
	E_CommandStatus(int value) {
		this.value = value;
	}
	public int getValue() {
		return this.value;
	}
}
