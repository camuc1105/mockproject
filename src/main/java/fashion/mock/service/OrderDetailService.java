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
		return orderDetailRepository.findAll().stream()
				.filter(orderDetail -> orderDetail.getOrder().getId().equals(orderId)).toList();
	}
}
