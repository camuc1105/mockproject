package fashion.mock.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import fashion.mock.model.Order;
import fashion.mock.repository.OrderRepository;

@Service
public class OrderService {

	private final OrderRepository orderRepository;

	public OrderService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	public Optional<Order> getOrderById(Long id) {
		return orderRepository.findById(id);
	}

}
