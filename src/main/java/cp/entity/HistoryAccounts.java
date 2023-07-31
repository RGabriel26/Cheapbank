package cp.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class HistoryAccounts {
	@Id
	public Long ID;
	public List<String> historyAccounts;
	public HistoryAccounts(Long iD, List<String> historyAccounts) {
		ID = iD;
		this.historyAccounts = historyAccounts;
	}
	public HistoryAccounts() {
	}
	public Long getID() {
		return ID;
	}
	public void setID(Long iD) {
		ID = iD;
	}
	public List<String> getHistoryAccounts() {
		return historyAccounts;
	}
	public void setHistoryAccounts(List<String> historyAccounts) {
		this.historyAccounts = historyAccounts;
	}
	
	

}
