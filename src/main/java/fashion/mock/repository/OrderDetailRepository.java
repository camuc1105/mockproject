package fashion.mock.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fashion.mock.model.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

}
