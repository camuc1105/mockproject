/**
 * @author Tran Thien Thanh 09/04/1996
 */
package fashion.mock.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fashion.mock.model.Order;
import fashion.mock.model.OrderDetail;
import fashion.mock.model.Payment;
import fashion.mock.model.TransactionHistory;
import fashion.mock.service.CheckoutService;
import fashion.mock.service.OrderDetailService;
import fashion.mock.service.OrderService;

@Controller
public class CheckoutController {

	private final OrderService orderService;
	private final OrderDetailService orderDetailService;
	private final CheckoutService checkoutService;

	public CheckoutController(OrderService orderService, OrderDetailService orderDetailService,
			CheckoutService checkoutService) {
		this.orderService = orderService;
		this.orderDetailService = orderDetailService;
		this.checkoutService = checkoutService;
	}

	@GetMapping("/checkout/{id}")
	public String getCheckoutPage(@PathVariable Long id, Model model) {
		Optional<Order> orderOptional = orderService.getOrderById(id);
		if (orderOptional.isPresent()) {
			Order order = orderOptional.get();
			List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(id);
			List<Payment> payments = checkoutService.getAllPayments();

			model.addAttribute("order", order);
			model.addAttribute("orderDetails", orderDetails);
			model.addAttribute("payments", payments);
			return "checkout"; // Tên của file Thymeleaf HTML
		} else {
			return "error"; // Trả về trang lỗi nếu không tìm thấy đơn hàng
		}
	}

	@PostMapping("/checkout/submit")
	public String submitCheckout(
			@RequestParam("userId") Long userId,
			@RequestParam("paymentMethod") String paymentMethod, 
			@RequestParam("shippingMethod") String shippingMethod,
			@RequestParam("totalPrice") Double totalPrice, @RequestParam("orderDetails") List<OrderDetail> orderDetails,
			Model model) {
		
		// Create new Order
        Order order = new Order();
        order.setUser(orderService.findUserById(userId)); // Assuming you have a method to get the User by ID
        order.setOrderDate(LocalDate.now());
        order.setTotalPrice(totalPrice);
        order.setStatus("Đang xử lý");
        
        Order savedOrder = orderService.saveOrder(order); // Save the new Order

        // Create new OrderDetails
        for (OrderDetail orderDetail : orderDetails) {
            orderDetail.setOrder(savedOrder); // Associate the OrderDetail with the new Order
            orderDetailService.saveOrderDetail(orderDetail);
        }
		
		// Create TransactionHistory
        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.setOrder(savedOrder);
        transactionHistory.setTransactionAmount(totalPrice);
        transactionHistory.setPayment(checkoutService.findPaymentByMethod(paymentMethod)); // Assuming you have a method to get Payment by method
        transactionHistory.setStatus(paymentMethod.equals("Tien mat") ? "Chua thanh toan" : "Da thanh toan");
        transactionHistory.setTransactionDate(paymentMethod.equals("Tien mat") ? null : LocalDate.now());
        checkoutService.saveTransactionHistory(transactionHistory);

        model.addAttribute("order", savedOrder);
        return "confirmation"; // Redirect to a confirmation page or similar
	}

}
