	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
package fashion.mock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import fashion.mock.model.Discount;
import fashion.mock.model.User;
import fashion.mock.service.DiscountService;
import fashion.mock.service.ProductService;
import fashion.mock.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/discounts")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;
    
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
    
    @GetMapping
    public String listDiscounts(Model model, HttpSession session, 
                                @RequestParam(defaultValue = "0") int page, 
                                @RequestParam(defaultValue = "5") int size, 
                                RedirectAttributes redirectAttributes) {
        if (!checkAdminAccess(session, model, redirectAttributes)) {
        	 return "403";
        }        
        
        Page<Discount> discountPage = discountService.getAllDiscounts(PageRequest.of(page, size));
        model.addAttribute("discounts", discountPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", discountPage.getTotalPages());
        model.addAttribute("totalItems", discountPage.getTotalElements());
        return "adminlistdiscount";
    }

    @GetMapping("/new")
    public String showAddDiscountForm(Model model, HttpSession session,RedirectAttributes redirectAttributes) {
        if (!checkAdminAccess(session, model, redirectAttributes)) {
        	 return "403";
        }        
        
        model.addAttribute("discount", new Discount());
        model.addAttribute("products", productService.getAllProducts());
        return "adminformdiscount";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateDiscountForm(@PathVariable Long id, Model model, HttpSession session,RedirectAttributes redirectAttributes) {
        if (!checkAdminAccess(session, model, redirectAttributes)) {
        	 return "403";
        }        
        
        Discount discount = discountService.getDiscountById(id)
                .orElseThrow(() -> new RuntimeException("Discount không tồn tại"));
        model.addAttribute("discount", discount);
        model.addAttribute("products", productService.getAllProducts());
        return "adminformdiscount";
    }

    @PostMapping
    public String addDiscount(@Valid @ModelAttribute("discount") Discount discount, BindingResult result,
                              Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("products", productService.getAllProducts());
            return "adminformdiscount";
        }
        try {
            discountService.addDiscount(discount);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm giảm giá thành công!");
            return "redirect:/discounts";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("discount", discount);
            return "redirect:/discounts/new";
        }
    }

    @PostMapping("/update")
    public String updateDiscount(@Valid @ModelAttribute("discount") Discount discount, BindingResult result,
                                 Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("products", productService.getAllProducts());
            return "adminformdiscount";
        }
        try {
            discountService.updateDiscount(discount);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật giảm giá thành công!");
            return "redirect:/discounts";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("discount", discount);
            return "redirect:/discounts/edit/" + discount.getId();
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteDiscount(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean deleted = discountService.deleteDiscount(id);
        if (!deleted) {
            redirectAttributes.addFlashAttribute("message", "Giảm giá không tồn tại");
            redirectAttributes.addFlashAttribute("messageType", "error");
        } else {
            redirectAttributes.addFlashAttribute("message", "Giảm giá đã được xóa thành công");
            redirectAttributes.addFlashAttribute("messageType", "success");
        }
        return "redirect:/discounts";
    }

    @GetMapping("/search")
    public String searchDiscounts(@RequestParam(value = "searchTerm", required = false) String searchTerm, 
                                  @RequestParam(defaultValue = "0") int page, 
                                  @RequestParam(defaultValue = "5") int size,
                                  Model model, HttpSession session,RedirectAttributes redirectAttributes) {
        if (!checkAdminAccess(session, model, redirectAttributes)) {
        	 return "403";
        }        
        if (page < 0) {
            page = 0;
        }
        Page<Discount> discountPage = discountService.searchDiscounts(searchTerm, PageRequest.of(page, size));
        model.addAttribute("discounts", discountPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", discountPage.getTotalPages());
        model.addAttribute("totalItems", discountPage.getTotalElements());
        model.addAttribute("searchTerm", searchTerm);
        return "adminlistdiscount";
    }
}