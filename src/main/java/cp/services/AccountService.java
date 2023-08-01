package cp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cp.entity.Account;
import repo.AccountRepository;

@Service("account")
public class AccountService {

	@Autowired()
	private AccountRepository accountRepo;
	
	
	public void saveNewAccount(Account newAccount) {
		accountRepo.save(newAccount);
	}
	
	public List<Account> getAllUsers() {
		return accountRepo.findAll();
	}

	public boolean existID(Long ID) {
		if(accountRepo.existsById(ID)) {
			return true;
		}else {
			return false;
		}
	}
	
	public String getUserPassword(Long ID) {
		return accountRepo.findById(ID).get().getPassword();
	}
	
	public String getUserFirstName(Long ID) {
		return accountRepo.findById(ID).get().getFirstName();
	}
	
	public String getUserLastName(Long ID) {
		return accountRepo.findById(ID).get().getLastName();
	}
	
	public Double getUserSold(Long ID) {
		return accountRepo.findById(ID).get().getSold();
	}
	
	public String getUserEmail(Long ID) {
		return accountRepo.findById(ID).get().getEmail();
	}
	
	public String getUserPhoneNumber(Long ID) {
		return accountRepo.findById(ID).get().getPhoneNumber();
	}
	
	
	

}
