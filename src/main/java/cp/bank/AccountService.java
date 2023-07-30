package cp.bank;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

	@Autowired
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
	
	public Optional<Account> getAccount(Long ID) {
		return accountRepo.findById(ID);
	}
	

}
