package Net.PC15.FC8800.Command.System.Result;
import Net.PC15.Command.INCommandResult;

import java.util.ArrayList;
public class SearchEquptOnNetNum_Result implements INCommandResult {
	public int SearchTotal = 0;
	public ArrayList ResultList;
	public synchronized void AddSearchResult(String SN, byte[] ResultData) {
		if (this.ResultList == null) {
			this.ResultList = new ArrayList(10);
		}
		this.ResultList.add(new SearchResult(SN, ResultData));
		++this.SearchTotal;
	}
	public void release() {
	}
	public class SearchResult {
		public String SN;
		public byte[] ResultData;
		public SearchResult(String SN, byte[] ResultData) {
			this.SN = SN;
			this.ResultData = ResultData;
		}
	}
}
