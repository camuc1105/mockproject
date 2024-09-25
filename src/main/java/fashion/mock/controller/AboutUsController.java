package fashion.mock.controller;

import fashion.mock.model.Category;
import fashion.mock.model.User;
import fashion.mock.service.CartItemService;
import fashion.mock.service.CategoryService;
import fashion.mock.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/aboutus")
public class AboutUsController {
    private final CategoryService categoryService;
    private CartItemService cartItemService;
    private UserService userService;

    public AboutUsController(CategoryService categoryService, CartItemService cartItemService, UserService userService) {
        super();
        this.categoryService = categoryService;
        this.cartItemService = cartItemService;
        this.userService = userService;
    }
    @GetMapping
    public String viewAboutUs(Model model, HttpSession session) {
        // Lấy tất cả danh mục
        List<Category> categories = categoryService.getAllCategories();
        // Phân loại danh mục dựa trên tên bắt đầu bằng "Áo" hoặc "Quần"
        List<Category> aoCategories = categories.stream()
                .filter(category -> category.getCategoryName().startsWith("Áo")).collect(Collectors.toList());
        List<Category> quanCategories = categories.stream()
                .filter(category -> category.getCategoryName().startsWith("Quần")).collect(Collectors.toList());
        // Thêm danh sách "Áo" và "Quần" vào model
        model.addAttribute("aoCategories", aoCategories);
        model.addAttribute("quanCategories", quanCategories);
        // thảo
        User user = (User) session.getAttribute("user");
        boolean isAdmin = false; // Initialize isAdmin
        if (user != null) {
            isAdmin = userService.isAdmin(user.getId());
            model.addAttribute("user", user);
            model.addAttribute("totalCartItems", cartItemService.getCount());

        } else {
            model.addAttribute("totalCartItems", "0");
        }
        model.addAttribute("isAdmin", isAdmin);
        return "aboutus";
    }
}
