package cp.bank;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoryTransfersService {
	
	@Autowired
	private HistoryTransferRepository historyTransfersRepo;
	
	public void saveNewTransactionsHistory(HistoryTransfers historyTransfers) {
		historyTransfersRepo.save(historyTransfers);
	}
	
	public boolean existID(Long ID) {
		if(historyTransfersRepo.existsById(ID)) {
			return true;
		}else {
			return false;
		}
	}
	
	public Optional<HistoryTransfers> getHistoryTransfers(Long ID) {
		return historyTransfersRepo.findById(ID);
	}
	
	
	
	
}
