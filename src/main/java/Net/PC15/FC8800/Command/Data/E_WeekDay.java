package Net.PC15.FC8800.Command.Data;
public enum E_WeekDay {
	Monday(0), Tuesday(1), Wednesday(2), Thursday(3), Friday(4), Saturday(5), Sunday(6);
	private final int value;
	E_WeekDay(int value) {
		this.value = value;
	}
	public int getValue() {
		return this.value;
	}
}
