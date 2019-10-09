package Net.PC15.Connector;
import org.jetbrains.annotations.Contract;
/**
 * @author zch
 */
public enum E_ControllerType {
	// 设备类型
	FC8900(0), FC8800(1), MC5800(2);
	private final int value;
	@Contract(pure = true)
	E_ControllerType(int value) {
		this.value = value;
	}
	@Contract(pure = true)
	public int getValue() {
		return this.value;
	}
}
