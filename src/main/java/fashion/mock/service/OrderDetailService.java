/**
 * @author Tran Thien Thanh 09/04/1996
 */
package fashion.mock.service;

import java.util.List;

import org.springframework.stereotype.Service;

import fashion.mock.model.OrderDetail;
import fashion.mock.repository.OrderDetailRepository;

@Service
public class OrderDetailService {

	private final OrderDetailRepository orderDetailRepository;

	public OrderDetailService(OrderDetailRepository orderDetailRepository) {
		this.orderDetailRepository = orderDetailRepository;
	}

	public List<OrderDetail> getOrderDetailsByOrderId(Long orderId) {
		return orderDetailRepository.findByOrderId(orderId);
	}

	public void saveOrderDetail(OrderDetail orderDetail) {
		orderDetailRepository.save(orderDetail);
	}

}
