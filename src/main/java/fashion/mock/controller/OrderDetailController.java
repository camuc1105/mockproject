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
import fashion.mock.model.User;
import fashion.mock.service.OrderDetailService;
import fashion.mock.service.OrderService;
import jakarta.servlet.http.HttpSession;

@Controller
public class OrderDetailController {

	private final OrderService orderService;
	private final OrderDetailService orderDetailService;

	public OrderDetailController(OrderService orderService, OrderDetailService orderDetailService) {
		this.orderService = orderService;
		this.orderDetailService = orderDetailService;
	}

	@GetMapping("/orderDetail/{id}")
	public String getOrderDetail(@PathVariable Long id, Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
	        return "redirect:/login/loginform"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
	    }
		
		Optional<Order> orderOptional = orderService.getOrderById(id);
		if (orderOptional.isEmpty()) {
			model.addAttribute("errorMessage", "Order not found.");
			return "error"; // Or a specific error page
		}
		Order order = orderOptional.get();
		
		// Check if the order belongs to the logged-in user
	    if (!order.getUser().getId().equals(user.getId())) {
	        model.addAttribute("errorMessage", "You do not have permission to view this order.");
	        return "404"; // Redirect to a specific error page or a permission-denied page
	    }
		
		List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(id);
		
		model.addAttribute("order", order);
		model.addAttribute("orderDetails", orderDetails);
		return "orderDetails";
	}

}
