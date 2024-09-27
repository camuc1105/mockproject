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
import org.springframework.web.bind.annotation.RequestMapping;

import fashion.mock.model.Order;
import fashion.mock.model.OrderDetail;
import fashion.mock.model.User;
import fashion.mock.service.OrderDetailService;
import fashion.mock.service.OrderService;
import fashion.mock.util.ShoppingCartUtils;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/orderDetail")
public class OrderDetailController {

	private final OrderService orderService;
	private final OrderDetailService orderDetailService;
	private final ShoppingCartUtils shoppingCartUtils;

	public OrderDetailController(OrderService orderService, OrderDetailService orderDetailService,
			ShoppingCartUtils shoppingCartUtils) {
		this.orderService = orderService;
		this.orderDetailService = orderDetailService;
		this.shoppingCartUtils = shoppingCartUtils;
	}

	@GetMapping("/{id}")
	public String getOrderDetail(@PathVariable Long id, Model model, HttpSession session) {
		String redirect = shoppingCartUtils.checkLoginAndCart(session, model);
		// If the utility method returned a redirect path, return it
		if (redirect != null) {
			return redirect;
		}
		// Prepare category info
        shoppingCartUtils.prepareCategoryInfo(model);
		
		User user = (User) session.getAttribute("user");
		
		Optional<Order> orderOptional = orderService.getOrderById(id);
		if (orderOptional.isEmpty()) {
			model.addAttribute("errorMessage", "Order not found.");
			return "404"; // Redirect to PageNotFound page
		}
		Order order = orderOptional.get();
		
		// Check if the order belongs to the logged-in user
	    if (!order.getUser().getId().equals(user.getId())) {
	        model.addAttribute("errorMessage", "You do not have permission to view this order.");
	        return "403"; // Redirect to permission-denied page
	    }
		
		List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(id);
		
		model.addAttribute("order", order);
		model.addAttribute("orderDetails", orderDetails);
		return "orderDetails";
	}

}
