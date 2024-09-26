/**
 * Author: Lê Nguyên Minh Quý 27/06/1998
 */
package fashion.mock.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
@RequestMapping("/shop")
public class ProductUserController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String listProducts(Model model, HttpSession session, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int max, @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String sortBy, @RequestParam(required = false) String color,
            @RequestParam(required = false) String size, @RequestParam(required = false) String priceRange,
            @RequestParam(required = false) Long categoryId) {
        
        List<Category> categories = categoryService.getAllCategories();
        List<Category> aoCategories = categories.stream()
                .filter(category -> category.getCategoryName().startsWith("Áo")).collect(Collectors.toList());
        List<Category> quanCategories = categories.stream()
                .filter(category -> category.getCategoryName().startsWith("Quần"))
                .collect(Collectors.toList());
        
        String categoryName = null;
        if (categoryId != null) {
            Optional<Category> categoryOpt = categoryService.getCategoryById(categoryId);
            Category category = categoryOpt.orElse(null);
            categoryName = category != null ? category.getCategoryName() : null;
        }

        PageRequest pageRequest = PageRequest.of(page, max);
        Page<Product> productPage = productService.getFilteredProducts(searchTerm, sortBy, color, size, priceRange,
                categoryId, pageRequest);

        List<Product> products = productPage.getContent();
        List<Boolean> productsOnDiscount = products.stream().map(productService::isProductOnDiscount)
                .collect(Collectors.toList());
        List<Double> discountedPrices = products.stream().map(productService::getDiscountedPrice)
                .collect(Collectors.toList());
        
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
        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("color", color);
        model.addAttribute("size", size);
        model.addAttribute("priceRange", priceRange);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("aoCategories", aoCategories);
        model.addAttribute("quanCategories", quanCategories);
        model.addAttribute("categories", categories);
        model.addAttribute("productsOnDiscount", productsOnDiscount);
        model.addAttribute("discountedPrices", discountedPrices);
        model.addAttribute("categoryName", categoryName);
        return "shop";
    }

    @GetMapping("/{id}")
    public String showProductDetail(@PathVariable Long id, Model model,HttpSession session) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm với id: " + id));
        
        List<Category> categories = categoryService.getAllCategories();
        List<Category> aoCategories = categories.stream()
                .filter(category -> category.getCategoryName().startsWith("Áo")).collect(Collectors.toList());
        List<Category> quanCategories = categories.stream()
                .filter(category -> category.getCategoryName().startsWith("Quần")).collect(Collectors.toList());

        boolean isOnDiscount = productService.isProductOnDiscount(product);
        double discountedPrice = productService.getDiscountedPrice(product);
        
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
        model.addAttribute("product", product);
        model.addAttribute("isOnDiscount", isOnDiscount);
        model.addAttribute("discountedPrice", discountedPrice);
        model.addAttribute("aoCategories", aoCategories);
        model.addAttribute("quanCategories", quanCategories);
        model.addAttribute("categories", categories);
        return "shopdetail";
    }
}