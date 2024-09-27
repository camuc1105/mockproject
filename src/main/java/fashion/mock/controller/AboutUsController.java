package fashion.mock.controller;

import fashion.mock.model.CartItem;
import fashion.mock.model.Category;
import fashion.mock.model.Product;
import fashion.mock.model.User;
import fashion.mock.service.CartItemService;
import fashion.mock.service.CategoryService;
import fashion.mock.service.ProductService;
import fashion.mock.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/aboutus")
public class AboutUsController {
    private final CategoryService categoryService;
    private final UserService userService;

    private final ProductService productService;

    public AboutUsController(CategoryService categoryService, UserService userService, ProductService productService) {
        super();
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
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
        /**
         * Author: Lê Nguyên Minh Quý 27/06/1998
         */
        List<Product> newProducts = productService.getTop4NewProducts();

        Random random = new Random();
        Category randomCategory = categories.get(random.nextInt(categories.size()));

        // Lấy sản phẩm của danh mục ngẫu nhiên
        List<Product> randomCategoryProducts = productService.getProductsByCategory(randomCategory.getCategoryName());

        List<Product> products = productService.getAllProducts();
        List<Boolean> productsOnDiscount = products.stream().map(productService::isProductOnDiscount)
                .collect(Collectors.toList());
        List<Double> discountedPrices = products.stream().map(productService::getDiscountedPrice)
                .collect(Collectors.toList());

        // Thêm danh sách "Áo" và "Quần" vào model
        model.addAttribute("aoCategories", aoCategories);
        model.addAttribute("quanCategories", quanCategories);
        model.addAttribute("newProducts", newProducts);
        model.addAttribute("products", products);
        model.addAttribute("productsOnDiscount", productsOnDiscount);
        model.addAttribute("discountedPrices", discountedPrices);
        model.addAttribute("randomCategoryProducts", randomCategoryProducts);
        model.addAttribute("randomCategory", randomCategory);

        @SuppressWarnings("unchecked")
        Map<Long, CartItem> cartItemsMap = (Map<Long, CartItem>) session.getAttribute("cartItems");
        if (cartItemsMap == null) {
            cartItemsMap = new HashMap<>();
            session.setAttribute("cartItems", cartItemsMap);
        }
        Collection<CartItem> cartItems = cartItemsMap.values();

        User user = (User) session.getAttribute("user");
        boolean isAdmin = false;

        if (user != null) {
            isAdmin = userService.isAdmin(user.getId());
            model.addAttribute("user", user);
            model.addAttribute("totalCartItems", cartItems.size());

        } else {
            model.addAttribute("totalCartItems", "0");
        }
        model.addAttribute("isAdmin", isAdmin);
        return "aboutus";
    }
}
