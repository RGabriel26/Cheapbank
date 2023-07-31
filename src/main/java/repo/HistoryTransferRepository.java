package repo;

import org.springframework.data.jpa.repository.JpaRepository;

import cp.entity.HistoryTransfers;

public interface HistoryTransferRepository extends JpaRepository<HistoryTransfers, Long>{

}
