/**
 * @author Tran Thien Thanh 09/04/1996
 */
package fashion.mock.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fashion.mock.exception.NotEnoughInStockException;
import fashion.mock.model.CartItem;
import fashion.mock.model.Order;
import fashion.mock.model.OrderDetail;
import fashion.mock.model.Payment;
import fashion.mock.model.Product;
import fashion.mock.model.TransactionHistory;
import fashion.mock.model.User;
import fashion.mock.service.CheckoutService;
import fashion.mock.service.EmailService;
import fashion.mock.service.OrderDetailService;
import fashion.mock.service.OrderService;
import fashion.mock.service.ProductService;
import fashion.mock.util.ShoppingCartUtils;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

	private static final String SELECTED_CART_ITEMS = "selectedCartItems";
	private static final String CART_ITEMS = "cartItems";

	private final ShoppingCartUtils shoppingCartUtils;
	private final CheckoutService checkoutService;
	private final OrderService orderService;
	private final OrderDetailService orderDetailService;
	private final ProductService productService;
	private final EmailService emailService;
	
	
public CheckoutController(ShoppingCartUtils shoppingCartUtils, CheckoutService checkoutService,
		OrderService orderService, OrderDetailService orderDetailService, ProductService productService,
		EmailService emailService) {
	this.shoppingCartUtils = shoppingCartUtils;
	this.checkoutService = checkoutService;
	this.orderService = orderService;
	this.orderDetailService = orderDetailService;
	this.productService = productService;
	this.emailService = emailService;
}
	@GetMapping
	public String showCheckoutPage(HttpSession session, Model model) {
		
		String redirect = shoppingCartUtils.checkLoginAndCart(session, model);
		// If the utility method returned a redirect path, return it
		if (redirect != null) {
			return redirect;
		}
		
		// Prepare category info
        shoppingCartUtils.prepareCategoryInfo(model);

		// Retrieve the selected cart items from session
		@SuppressWarnings("unchecked")
		List<CartItem> selectedCartItems = (List<CartItem>) session.getAttribute(SELECTED_CART_ITEMS);
		List<Payment> payments = checkoutService.getAllPaymentsAvailable();

		// Ensure items were selected
		if (selectedCartItems == null || selectedCartItems.isEmpty()) {
			return "redirect:/shopping-cart/view"; // If no items, redirect back to cart
		}

		// Calculate total price based on selected items (sum of price * quantity)
		double totalPrice = selectedCartItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();

		// Pass selected items to model
		model.addAttribute(SELECTED_CART_ITEMS, selectedCartItems);
		model.addAttribute("payments", payments); // Add the user to the model
		model.addAttribute("totalPrice", totalPrice); // Pass the total price to the view

		// Render checkout page (Thymeleaf template)
		return "checkout";
	}

	@PostMapping("/submit")
	public String submitCheckout(@RequestParam String paymentMethod, @RequestParam String shippingMethod,
			HttpSession session, Model model) {

		User user = (User) session.getAttribute("user");
		Long userId = user.getId();

		// Retrieve the selected items from the session
		@SuppressWarnings("unchecked")
		List<CartItem> selectedItems = (List<CartItem>) session.getAttribute(SELECTED_CART_ITEMS);

		// Calculate total price
		double totalPrice = selectedItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();

		// Convert shippingMethod to a double for shipping cost, Calculate total price
		double shippingCost = Double.parseDouble(shippingMethod);
		double totalPriceWithShipping = totalPrice + shippingCost;

		// Create a new order and save the order details
		Order order = new Order();
		order.setUser(orderService.findUserById(userId));
		order.setOrderDate(LocalDate.now());
		order.setShippingPrice(shippingCost);
		order.setTotalPrice(totalPriceWithShipping);
		order.setStatus("Chờ phê duyệt"); // Change status according to payment method
		order.setUpdatedDate(LocalDate.now());
		Order savedOrder = orderService.saveOrder(order);

		// Save each item as an OrderDetail
		for (CartItem cartItem : selectedItems) {
			// Get the product from the database
			Product product = productService.getProductById(cartItem.getProductID())
					.orElseThrow(() -> new RuntimeException("Product not found"));

			// Calculate new quantity for the product in stock
			int newQuantity = product.getQuantity() - cartItem.getQuantity();
			if (newQuantity < 0) {
				throw new NotEnoughInStockException("Số lượng sản phẩm trong kho không đủ: " + product.getProductName());
			}

			// Update the product quantity in the database
			product.setQuantity(newQuantity);
			productService.updateProduct(product);

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
		transactionHistory.setTransactionAmount(totalPriceWithShipping);
		if (paymentMethod.equalsIgnoreCase("Tiền mặt")) {
			transactionHistory.setStatus("Chờ thanh toán");
		} else {
			transactionHistory.setTransactionDate(LocalDate.now());
			transactionHistory.setStatus("Hoàn tất");
		}
		checkoutService.saveTransactionHistory(transactionHistory);

		// Pass the order object to the model so it can be used in the Thymeleaf
		// template
		model.addAttribute("order", savedOrder);
		model.addAttribute("totalPrice", totalPrice);

		// Remove checked-out items from cart and update session
		@SuppressWarnings("unchecked")
		Map<Long, CartItem> cartItemsMap = (Map<Long, CartItem>) session.getAttribute(CART_ITEMS);
		for (CartItem item : selectedItems) {
			cartItemsMap.remove(item.getProductID()); // Remove from cart
		}
		session.setAttribute(CART_ITEMS, cartItemsMap);

		// Send confirmation email to the user
		String userEmail = user.getEmail(); // Get the user's email
		emailService.sendOrderConfirmation(userEmail, savedOrder); // Call email service

		// Clear the selected items after checkout
		session.removeAttribute(SELECTED_CART_ITEMS);

		// Redirect to confirmation page
		return "redirect:/information/purchase-history";
	}

}
