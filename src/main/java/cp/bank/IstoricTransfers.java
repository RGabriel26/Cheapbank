package cp.bank;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class IstoricTransfers {
	
	@Id
	public Long ID;
	public List<String> transactions;
	
	public IstoricTransfers(Long iD, List<String> transactions) {
		super();
		ID = iD;
		this.transactions = transactions;
	}
	
	public IstoricTransfers() {
		
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
