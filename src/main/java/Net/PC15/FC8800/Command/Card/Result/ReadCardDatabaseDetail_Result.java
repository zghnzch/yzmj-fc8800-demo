package Net.PC15.FC8800.Command.Card.Result;
import Net.PC15.Command.INCommandResult;
public class ReadCardDatabaseDetail_Result implements INCommandResult {
	public long SortDataBaseSize;
	public long SortCardSize;
	public long SequenceDataBaseSize;
	public long SequenceCardSize;
	@Override
	public void release() {
	}
}
