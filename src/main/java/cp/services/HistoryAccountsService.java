package cp.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cp.entity.HistoryAccounts;
import repo.HistoryAccountsRepository;

@Service("historyAccounts")
public class HistoryAccountsService {

	@Autowired
	private HistoryAccountsRepository historyAccoutsRepo;
	
	public void saveNewAccountsHistory(HistoryAccounts historyAccount) {
		historyAccoutsRepo.save(historyAccount);
	}

	public boolean existID(Long ID) {
		if(historyAccoutsRepo.existsById(ID)) {
			return true;
		}else {
			return false;
		}
	}
	
	public Optional<HistoryAccounts> getHistoryAccount(Long ID) {
		return historyAccoutsRepo.findById(ID);
	}
}
