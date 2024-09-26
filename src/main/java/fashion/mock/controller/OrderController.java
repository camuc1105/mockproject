/**
 * @author Duong Van Luc 01/07/2000
 */
package fashion.mock.controller;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fashion.mock.model.Order;
import fashion.mock.model.User;
import fashion.mock.service.OrderService;
import fashion.mock.service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }
    
    private boolean checkAdminAccess(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null || !userService.isAdmin(user.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền truy cập trang này.");
            return false;
        }
        model.addAttribute("user", user);
        model.addAttribute("isAdmin", true);
        return true;
    }

    @GetMapping("")
    public String getAllOrders(HttpSession session, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size, @RequestParam(required = false) Long searchTerm, Model model,
            RedirectAttributes redirectAttributes) {
//        User user = (User) session.getAttribute("user");
        
        if (!checkAdminAccess(session, model, redirectAttributes)) {
            return "403";
        }  

        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orderPage;
//        Page<Order> orderPage = orderService.getOrdersWithPagination(pageable);
        if (searchTerm != null) {
            orderPage = orderService.findById(searchTerm, pageable);
        } else {
            orderPage = orderService.getOrdersWithPagination(pageable);
        }

        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("currentPage", orderPage.getNumber());
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("pageSize", orderPage.getSize());
        model.addAttribute("hasPrevious", orderPage.hasPrevious());
        model.addAttribute("hasNext", orderPage.hasNext());
        model.addAttribute("isFirst", orderPage.isFirst());
        model.addAttribute("isLast", orderPage.isLast());

        return "order-status";
    }

    @PostMapping("/updateStatus")
    public String updateOrderStatus(HttpSession session, Long id, String status) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login/loginform"; 
        }

        boolean isAdmin = userService.isAdmin(user.getId());
        if (!isAdmin) {
            return "403"; 
        }

        orderService.updateOrderStatus(id, status);
        return "redirect:/order";
    }

    @GetMapping("/403")
    public String accessDenied() {
        return "403";
    }
}
