package fashion.mock.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fashion.mock.model.CartItem;
import fashion.mock.model.Category;
import fashion.mock.model.Product;
import fashion.mock.model.User;
import fashion.mock.service.CartItemService;
import fashion.mock.service.CategoryService;
import fashion.mock.service.ProductService;
import fashion.mock.service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/home")
public class HomeController {

	private final CategoryService categoryService;
	private UserService userService;
	private final ProductService productService;

	public HomeController(CategoryService categoryService, CartItemService cartItemService, UserService userService,ProductService productService) {
		super();
		this.categoryService = categoryService;
		this.userService = userService;
		this.productService = productService;
	}

	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
	@GetMapping
	public String listNavigation(Model model, HttpSession session) {
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
		List<Product> aoSoMiProducts = productService.getProductsByCategory("Áo sơ mi");
		
		List<Product> products = productService.getAllProducts();
		List<Boolean> productsOnDiscount = products.stream().map(productService::isProductOnDiscount)
                .collect(Collectors.toList());
        List<Double> discountedPrices = products.stream().map(productService::getDiscountedPrice)
                .collect(Collectors.toList());
        
        Category aoSoMiCategory = categories.stream()
            .filter(category -> category.getCategoryName().equalsIgnoreCase("Áo sơ mi"))
            .findFirst().orElse(null);

		// Thêm danh sách "Áo" và "Quần" vào model
		model.addAttribute("aoCategories", aoCategories);
		model.addAttribute("quanCategories", quanCategories);
		model.addAttribute("products", aoSoMiProducts);
		model.addAttribute("products", products);
	    model.addAttribute("productsOnDiscount", productsOnDiscount);
	    model.addAttribute("discountedPrices", discountedPrices);
	    model.addAttribute("aoSoMiCategory", aoSoMiCategory);
	
	    @SuppressWarnings("unchecked")
		Map<Long, CartItem> cartItemsMap = (Map<Long, CartItem>) session.getAttribute("cartItems");
        if (cartItemsMap == null) {
            cartItemsMap = new HashMap<>(); // Initialize if not set
            session.setAttribute("cartItems", cartItemsMap);
        }
	    Collection<CartItem> cartItems = cartItemsMap.values();
		
		// thảo
		
		User user = (User) session.getAttribute("user");
		boolean isAdmin = false; // Initialize isAdmin

		if (user != null) {
		    isAdmin = userService.isAdmin(user.getId());
		    model.addAttribute("user", user);
		    model.addAttribute("totalCartItems", cartItems.size());

		} else {
			model.addAttribute("totalCartItems", "0");
		}
		model.addAttribute("isAdmin", isAdmin);
		return "home";
	}

}
