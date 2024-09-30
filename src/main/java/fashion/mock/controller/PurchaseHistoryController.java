/**
 * @author Duong Van Luc 01/07/2000
 */
package fashion.mock.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fashion.mock.model.User;
import fashion.mock.model.DTO.TransactionHistoryDTO;
import fashion.mock.service.CustomerInformationService;
import fashion.mock.service.PurchaseHistoryService;
import fashion.mock.service.UserService;
import fashion.mock.util.ShoppingCartUtils;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/information")
public class PurchaseHistoryController {

    private final ShoppingCartUtils shoppingCartUtils;
    private final PurchaseHistoryService purchaseHistoryService;
    private final CustomerInformationService customerInformationService;
    private final UserService userService;

    public PurchaseHistoryController(ShoppingCartUtils shoppingCartUtils, PurchaseHistoryService purchaseHistoryService,
            CustomerInformationService customerInformationService, UserService userService) {
        this.shoppingCartUtils = shoppingCartUtils;
        this.purchaseHistoryService = purchaseHistoryService;
        this.customerInformationService = customerInformationService;

        this.userService = userService;

    }

    @GetMapping("purchase-history")
    public String viewPurchaseHistory(HttpSession session, @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size, Model model) {
        String redirect = shoppingCartUtils.checkLoginAndCart(session, model);
        // If the utility method returned a redirect path, return it
        if (redirect != null) {
            return redirect;
        }

        // Prepare category info
        shoppingCartUtils.prepareCategoryInfo(model);

        User user = (User) session.getAttribute("user");
        boolean isAdmin = false; // Initialize isAdmin

        if (user != null) {
            isAdmin = userService.isAdmin(user.getId());
            model.addAttribute("user", user);
        } else {
            return "redirect:/login/loginform";
        }
        model.addAttribute("isAdmin", isAdmin);

        Long userId = user.getId(); // Get userId From User in session

        User user1 = customerInformationService.getUserById(userId);
        model.addAttribute("user", user1);

        Pageable pageable = PageRequest.of(page, size);
        Page<TransactionHistoryDTO> transactionHistoriesPage = purchaseHistoryService
                .findAllTransactionHistoriesByUserId(userId, pageable);

        model.addAttribute("transactionHistoriesPage", transactionHistoriesPage);
        return "purchase-history";
    }

    @PostMapping("/cancel-order/{orderId}")
    public String cancelOrder(@PathVariable Long orderId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            model.addAttribute("error", "Bạn cần đăng nhập để hủy đơn hàng.");
            return "redirect:/login/loginform";
        }

        boolean success = purchaseHistoryService.cancelOrder(orderId, user.getId());

        if (success) {
            model.addAttribute("message", "Đơn hàng đã được hủy thành công.");
        } else {
            model.addAttribute("error", "Không thể hủy đơn hàng.");
        }

        return "redirect:/information/purchase-history";  
    }

    
    @GetMapping("customer-information")
    public String viewCustomerInformation(HttpSession session, Model model) {
        return "customer-information";
    }
}
