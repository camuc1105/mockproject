/**
 * @author Tran Thien Thanh 09/04/1996
 */
package fashion.mock.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fashion.mock.model.CartItem;
import fashion.mock.model.Order;
import fashion.mock.model.OrderDetail;
import fashion.mock.model.Payment;
import fashion.mock.model.User;
import fashion.mock.service.CheckoutService;
import fashion.mock.service.OrderDetailService;
import fashion.mock.service.OrderService;
import fashion.mock.service.ProductService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

	private final OrderService orderService;
	private final OrderDetailService orderDetailService;
	private final CheckoutService checkoutService;
	private final ProductService productService;

	public CheckoutController(OrderService orderService, OrderDetailService orderDetailService,
			CheckoutService checkoutService, ProductService productService) {
		this.orderService = orderService;
		this.orderDetailService = orderDetailService;
		this.checkoutService = checkoutService;
		this.productService = productService;
	}
	
	@GetMapping
	public String showCheckoutPage(HttpSession session, Model model) {
		// Temporarily hardcode a userId for testing
	    Long userId = 1L;  // Replace with an actual user ID that exists in your database
	    // Fetch the user from the database using the hardcoded ID
	    User user = orderService.findUserById(userId);
	    if (user == null) {
	        throw new RuntimeException("User not found"); // Handle if user is not found
	    }
		
		
		
		
	    // Retrieve the selected cart items from session
	    @SuppressWarnings("unchecked")
	    List<CartItem> selectedCartItems = (List<CartItem>) session.getAttribute("selectedCartItems");
	    List<Payment> payments = checkoutService.getAllPayments();

	    // Ensure items were selected
	    if (selectedCartItems == null || selectedCartItems.isEmpty()) {
	        return "redirect:/shopping-cart/view"; // If no items, redirect back to cart
	    }

	    // Pass selected items to model
	    model.addAttribute("selectedCartItems", selectedCartItems);
	    model.addAttribute("user", user);  // Add the user to the model
	    model.addAttribute("payments", payments);  // Add the user to the model

	    // Render checkout page (Thymeleaf template)
	    return "checkout";
	}

	@PostMapping("/submit")
	public String submitCheckout(
//								@RequestParam Long userId,
	                             @RequestParam String paymentMethod,
	                             @RequestParam String shippingMethod,
//	                             @RequestParam Double totalPrice,
	                             HttpSession session, Model model) {
		
		// Temporarily hardcode a userId for testing
		Long userId = 1L;  // Replace with an actual user ID from your database

	    // Fetch the user from the database
	    User user = orderService.findUserById(userId);
	    if (user == null) {
	        throw new RuntimeException("User not found"); // Handle if user is not found
	    }
		
		
		
		
	    // Retrieve the selected items from the session
	    @SuppressWarnings("unchecked")
	    List<CartItem> selectedItems = (List<CartItem>) session.getAttribute("selectedCartItems");

	    // Ensure items exist before proceeding with checkout
	    if (selectedItems == null || selectedItems.isEmpty()) {
	        return "redirect:/shopping-cart/view"; // Redirect if no items selected
	    }
	    
	    // Calculate total price based on selected items (sum of price * quantity)
	    double totalPrice = 0;
	    for (CartItem cartItem : selectedItems) {
	        totalPrice += cartItem.getPrice() * cartItem.getQuantity();
	    }

	    // Create a new order and save the order details
	    Order order = new Order();
	    order.setUser(orderService.findUserById(userId));
	    order.setOrderDate(LocalDate.now());
	    order.setTotalPrice(totalPrice);
	    order.setStatus("Processing");

	    // Save the order to the database
	    Order savedOrder = orderService.saveOrder(order);

	    // Save each item as an OrderDetail
	    for (CartItem cartItem : selectedItems) {
	        OrderDetail orderDetail = new OrderDetail();
	        orderDetail.setOrder(savedOrder);
	        orderDetail.setProduct(productService.getProductById(cartItem.getProductID()).orElse(null));
	        orderDetail.setQuantity(cartItem.getQuantity());
	        orderDetail.setSubTotal(cartItem.getQuantity() * cartItem.getPrice());
	        orderDetailService.saveOrderDetail(orderDetail);
	    }
	    
	    // Pass the order object to the model so it can be used in the Thymeleaf template
	    model.addAttribute("order", savedOrder);
	    model.addAttribute("totalPrice", totalPrice);

	    // Clear the cart after successful checkout
	    session.removeAttribute("cartItems");
	    session.removeAttribute("selectedCartItems");

	    // Redirect to confirmation page
	    return "redirect:/confirmation";
	}

}
