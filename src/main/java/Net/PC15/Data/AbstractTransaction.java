package Net.PC15.Data;
import java.util.Calendar;
public abstract class AbstractTransaction implements INData {
	public int SerialNumber;
	protected Calendar _TransactionDate;
	protected short _TransactionType;
	protected short _TransactionCode;
	protected boolean _IsNull;
	public Calendar TransactionDate() {
		return this._TransactionDate;
	}
	public boolean IsNull() {
		return this._IsNull;
	}
	public short TransactionType() {
		return this._TransactionType;
	}
	public short TransactionCode() {
		return this._TransactionCode;
	}
}
