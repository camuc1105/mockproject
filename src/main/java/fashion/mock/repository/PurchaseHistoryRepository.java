package fashion.mock.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fashion.mock.model.TransactionHistory;
import fashion.mock.model.DTO.TransactionHistoryDTO;

@Repository
public interface PurchaseHistoryRepository extends JpaRepository<TransactionHistory, Long> {
    @Query("SELECT new fashion.mock.model.DTO.TransactionHistoryDTO(th.order.id, o.orderDate, o.status, th.transactionDate, th.transactionAmount, th.status) "
            + "FROM TransactionHistory th "
            + "JOIN th.order o")
//    List<TransactionHistoryDTO> findAllTransactionHistories();
    Page<TransactionHistoryDTO> findAllTransactionHistories(Pageable pageable);
}
