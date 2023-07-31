package repo;

import org.springframework.data.jpa.repository.JpaRepository;

import cp.entity.HistoryAccounts;

public interface HistoryAccountsRepository extends JpaRepository<HistoryAccounts, Long>{

}
