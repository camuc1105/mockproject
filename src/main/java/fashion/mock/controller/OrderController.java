/**
 * @author Duong Van Luc 01/07/2000
 */
package fashion.mock.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fashion.mock.model.Order;
import fashion.mock.service.OrderService;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("")
    public String getAllOrders(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Order> orderPage = orderService.getOrdersWithPagination(pageable);

        model.addAttribute("orders", orderPage.getContent());  // Lấy danh sách đơn hàng hiện tại
        model.addAttribute("currentPage", orderPage.getNumber());  // Trang hiện tại
        model.addAttribute("totalPages", orderPage.getTotalPages());  // Tổng số trang
        model.addAttribute("pageSize", orderPage.getSize());  // Kích thước trang
        model.addAttribute("hasPrevious", orderPage.hasPrevious());  // Kiểm tra trang trước
        model.addAttribute("hasNext", orderPage.hasNext());  // Kiểm tra trang sau
        model.addAttribute("isFirst", orderPage.isFirst());  // Kiểm tra trang đầu
        model.addAttribute("isLast", orderPage.isLast());  // Kiểm tra trang cuối

        return "order-status";
    }

    @PostMapping("/updateStatus")
    public String updateOrderStatus(Long id, String status) {
        orderService.updateOrderStatus(id, status);
        return "redirect:/order";
    }
}
