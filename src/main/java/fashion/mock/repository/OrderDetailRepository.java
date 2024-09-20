/**
 * @author Tran Thien Thanh 09/04/1996
 */
package fashion.mock.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fashion.mock.model.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
	
	List<OrderDetail> findByOrderId(Long orderId);

}
