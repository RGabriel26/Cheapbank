package cp.bank;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class IstoricAccounts {
	@Id
	public Long ID;
	public List<String> istoricAccounts;
	public IstoricAccounts(Long iD, List<String> istoricAccounts) {
		ID = iD;
		this.istoricAccounts = istoricAccounts;
	}
	public IstoricAccounts() {
	}
	public Long getID() {
		return ID;
	}
	public void setID(Long iD) {
		ID = iD;
	}
	public List<String> getIstoricAccounts() {
		return istoricAccounts;
	}
	public void setIstoricAccounts(List<String> istoricAccounts) {
		this.istoricAccounts = istoricAccounts;
	}
	
	

}
