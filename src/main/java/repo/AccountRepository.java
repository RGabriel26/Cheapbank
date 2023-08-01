package repo;

import org.springframework.data.jpa.repository.JpaRepository;

import cp.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
	

}
