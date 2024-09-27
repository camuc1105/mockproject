package fashion.mock.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import fashion.mock.model.CartItem;
import fashion.mock.model.Category;
import fashion.mock.model.User;
import fashion.mock.service.CategoryService;
import jakarta.servlet.http.HttpSession;

@Component
public class ShoppingCartUtils {
	
	private static final String CART_ITEMS = "cartItems";

	private final CategoryService categoryService;

	public ShoppingCartUtils(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	// Method to check user login and prepare cart information
    public String checkLoginAndCart(HttpSession session, Model model) {
        // Check User Login. If not, redirect to login page
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login/loginform";
        }

        // Retrieve cart items from session or initialize if not set
        @SuppressWarnings("unchecked")
        Map<Long, CartItem> cartItemsMap = (Map<Long, CartItem>) session.getAttribute(CART_ITEMS);
        if (cartItemsMap == null) {
            cartItemsMap = new HashMap<>();
            session.setAttribute(CART_ITEMS, cartItemsMap);
        }

        // Add cart items to model
        Collection<CartItem> cartItems = cartItemsMap.values();
        model.addAttribute(CART_ITEMS, cartItems);
        model.addAttribute("totalCartItems", cartItems.size());

        // Add user info to model
        model.addAttribute("user", user);

        return null; // Indicating no redirect needed
    }

    // Method to prepare category information
    public void prepareCategoryInfo(Model model) {
        // Fetch all categories
        List<Category> categories = categoryService.getAllCategories();

        // Filter and categorize based on the category name
        List<Category> aoCategories = categories.stream()
                .filter(category -> category.getCategoryName().startsWith("Áo"))
                .collect(Collectors.toList());
        List<Category> quanCategories = categories.stream()
                .filter(category -> category.getCategoryName().startsWith("Quần"))
                .collect(Collectors.toList());

        // Add categories to model
        model.addAttribute("aoCategories", aoCategories);
        model.addAttribute("quanCategories", quanCategories);
    }

}
