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
import fashion.mock.model.Payment;
import fashion.mock.service.OrderDetailService;
import fashion.mock.service.OrderService;
import fashion.mock.service.PaymentService;

@Controller
public class PaymentController {

	private final OrderService orderService;
	private final OrderDetailService orderDetailService;
	private final PaymentService paymentService;

	public PaymentController(OrderService orderService, OrderDetailService orderDetailService,
			PaymentService paymentService) {
		this.orderService = orderService;
		this.orderDetailService = orderDetailService;
		this.paymentService = paymentService;
	}

	@GetMapping("/payment/{id}")
	public String getPaymentPage(@PathVariable Long id, Model model) {
		Optional<Order> orderOptional = orderService.getOrderById(id);
		if (orderOptional.isPresent()) {
			Order order = orderOptional.get();
			List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(id);
			List<Payment> payments = paymentService.getAllPayments();

			model.addAttribute("order", order);
			model.addAttribute("orderDetails", orderDetails);
			model.addAttribute("payments", payments);
			return "payment"; // Tên của file Thymeleaf HTML
		} else {
			return "error"; // Trả về trang lỗi nếu không tìm thấy đơn hàng
		}
	}

}
