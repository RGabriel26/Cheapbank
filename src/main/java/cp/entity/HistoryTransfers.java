package cp.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class HistoryTransfers {
	
	@Id
	public Long ID;
	public List<String> transactions;
	
	public HistoryTransfers(Long iD, List<String> transactions) {

		ID = iD;
		this.transactions = transactions;
	}
	
	public HistoryTransfers() {
		
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long iD) {
		ID = iD;
	}

	public List<String> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<String> transactions) {
		this.transactions = transactions;
	}
	
}
