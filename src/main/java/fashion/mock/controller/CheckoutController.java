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
import fashion.mock.model.TransactionHistory;
import fashion.mock.model.User;
import fashion.mock.service.CheckoutService;
import fashion.mock.service.EmailService;
import fashion.mock.service.OrderDetailService;
import fashion.mock.service.OrderService;
import fashion.mock.service.ProductService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

	private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final ProductService productService;
    private final CheckoutService checkoutService;
    private final EmailService emailService;

    public CheckoutController(OrderService orderService, OrderDetailService orderDetailService,
			CheckoutService checkoutService, ProductService productService, EmailService emailService) {
		this.orderService = orderService;
		this.orderDetailService = orderDetailService;
		this.checkoutService = checkoutService;
		this.productService = productService;
		this.emailService = emailService;
	}

	@GetMapping
	public String showCheckoutPage(HttpSession session, Model model) {
		
		User user = (User) session.getAttribute("user");
		if (user == null) {
	        return "redirect:/login/loginform"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
	    }

		// Retrieve the selected cart items from session
		@SuppressWarnings("unchecked")
		List<CartItem> selectedCartItems = (List<CartItem>) session.getAttribute("selectedCartItems");
		List<Payment> payments = checkoutService.getAllPayments();

		// Ensure items were selected
		if (selectedCartItems == null || selectedCartItems.isEmpty()) {
			return "redirect:/shopping-cart/view"; // If no items, redirect back to cart
		}

		// Calculate total price based on selected items (sum of price * quantity)
		double totalPrice = 0;
		for (CartItem cartItem : selectedCartItems) {
			totalPrice += cartItem.getPrice() * cartItem.getQuantity();
		}

		// Pass selected items to model
		model.addAttribute("selectedCartItems", selectedCartItems);
		model.addAttribute("user", user); // Add the user to the model
		model.addAttribute("payments", payments); // Add the user to the model
		model.addAttribute("totalPrice", totalPrice); // Pass the total price to the view

		// Render checkout page (Thymeleaf template)
		return "checkout";
	}

	@PostMapping("/submit")
	public String submitCheckout(
//			@RequestParam Long userId,
			@RequestParam String paymentMethod, 
			@RequestParam String shippingMethod,
			HttpSession session, Model model) {
		
		User user = (User) session.getAttribute("user");
		Long userId = user.getId(); // Replace with an actual user ID from your database

		// Retrieve the selected items from the session
		@SuppressWarnings("unchecked")
		List<CartItem> selectedItems = (List<CartItem>) session.getAttribute("selectedCartItems");
		
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
		order.setStatus("Chờ phê duyệt"); // Change status according to payment method
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
		
		// Use CheckoutService to find payment method and save transaction history
	    Payment selectedPayment = checkoutService.findPaymentByMethod(paymentMethod);
	    TransactionHistory transactionHistory = new TransactionHistory();
	    transactionHistory.setOrder(savedOrder);
	    transactionHistory.setPayment(selectedPayment);
	    transactionHistory.setTransactionDate(LocalDate.now());
	    transactionHistory.setTransactionAmount(totalPrice);
	    transactionHistory.setStatus("Hoàn tất"); // Change status according to payment method
	    checkoutService.saveTransactionHistory(transactionHistory);

		// Pass the order object to the model so it can be used in the Thymeleaf template
		model.addAttribute("order", savedOrder);
		model.addAttribute("totalPrice", totalPrice);
		
		// Send confirmation email to the user
		String userEmail = user.getEmail(); // Get the user's email
		emailService.sendOrderConfirmation(userEmail, savedOrder); // Call email service

		// Clear the cart after successful checkout
		session.removeAttribute("cartItems");
		session.removeAttribute("selectedCartItems");

		// Redirect to confirmation page
		return "redirect:/information/purchase-history";
	}

}
