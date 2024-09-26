/**
 * @author Tran Thien Thanh 09/04/1996
 */
package fashion.mock.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import fashion.mock.model.Order;

/**
 * @author Tran Thien Thanh 01/07/2000
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findById(Long id, Pageable pageable);
}
