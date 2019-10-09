package Net.PC15.FC8800.Command.Transaction;
public enum e_TransactionDatabaseType {
	OnCardTransaction(1), OnButtonTransaction(2), OnDoorSensorTransaction(3), OnSoftwareTransaction(4), OnAlarmTransaction(5), OnSystemTransaction(6);
	private final int value;
	e_TransactionDatabaseType(int value) {
		this.value = value;
	}
	public static e_TransactionDatabaseType valueOf(int iValue) {
		e_TransactionDatabaseType[] lst = values();
		e_TransactionDatabaseType[] var2 = lst;
		int var3 = lst.length;
		for (int var4 = 0; var4 < var3; ++var4) {
			e_TransactionDatabaseType t = var2[var4];
			if (t.getValue() == iValue) {
				return t;
			}
		}
		return null;
	}
	public int getValue() {
		return this.value;
	}
}
