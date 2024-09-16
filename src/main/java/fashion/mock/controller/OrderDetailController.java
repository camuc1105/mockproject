/**
 * @author Tran Thien Thanh 09/04/1996
 */
package fashion.mock.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import fashion.mock.model.Order;
import fashion.mock.model.OrderDetail;
import fashion.mock.service.OrderDetailService;
import fashion.mock.service.OrderService;

@Controller
public class OrderDetailController {

	private final OrderService orderService;
	private final OrderDetailService orderDetailService;

	public OrderDetailController(OrderService orderService, OrderDetailService orderDetailService) {
		this.orderService = orderService;
		this.orderDetailService = orderDetailService;
	}

	@GetMapping("/orderDetail/{id}")
	public String getOrderDetail(@PathVariable Long id, Model model) {
		Optional<Order> orderOptional = orderService.getOrderById(id);
		if (orderOptional.isPresent()) {
			Order order = orderOptional.get();
			List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(id);

			model.addAttribute("order", order);
			model.addAttribute("orderDetails", orderDetails);
			return "orderDetails"; // Tên của file Thymeleaf HTML
		} else {
			return "error"; // Trả về trang lỗi nếu không tìm thấy đơn hàng
		}
	}

}
