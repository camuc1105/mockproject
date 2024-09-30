/**
 * @author Tran Thien Thanh 09/04/1996
 */
package fashion.mock.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import fashion.mock.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
/**
 * @author Duong Van Luc 01/07/2000
 */
    Page<Order> findById(Long id, Pageable pageable);

    Optional<Order> findByIdAndUserId(Long orderId, Long userId);
}
