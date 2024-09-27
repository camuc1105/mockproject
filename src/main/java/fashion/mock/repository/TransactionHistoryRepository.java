/**
 * @author Tran Thien Thanh 09/04/1996
 */
package fashion.mock.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fashion.mock.model.TransactionHistory;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long>{
    Optional<TransactionHistory> findByOrderId(Long orderId);
}
