/**
 * Author: Nguyen Cong Huan. 06/03/1999
 */
package fashion.mock.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
@RequestMapping("/shopping-cart")
public class ShoppingCartController {

	private final CartItemService cartItemService;
	private final CategoryService categoryService;
	private final UserService userService;
	private final ProductService productService;

	public ShoppingCartController(CartItemService cartItemService, CategoryService categoryService,
			UserService userService, ProductService productService) {
		this.cartItemService = cartItemService;
		this.categoryService = categoryService;
		this.userService = userService;
		this.productService = productService;
	}

	@GetMapping("/view")
	public String viewCart(Model model, HttpSession session) {
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

		// Lấy giỏ hàng từ session hoặc khởi tạo mới nếu chưa có
		@SuppressWarnings("unchecked")
        Map<Long, CartItem> cartItemsMap = (Map<Long, CartItem>) session.getAttribute("cartItems");

        if (cartItemsMap == null) {
            cartItemsMap = new HashMap<>(); // Initialize if not set
            session.setAttribute("cartItems", cartItemsMap);
        }
		
        // Get the cartItems as a Collection to pass to the model
        Collection<CartItem> cartItems = cartItemsMap.values();
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalCartItems", cartItems.size());

		User user = (User) session.getAttribute("user");
		boolean isAdmin = false; // Initialize isAdmin

		if (user != null) {
			isAdmin = userService.isAdmin(user.getId());
			model.addAttribute("user", user);
		} else {
			return "redirect:/login/loginform";
		}
		model.addAttribute("isAdmin", isAdmin);
		return "cart-item";
	}

	@PostMapping("/add")
	public String addCart(@RequestParam Long productId,
			@RequestParam String productName, 
			@RequestParam double price,
			@RequestParam int quantity, 
			@RequestParam String action, 
			@RequestParam String imgLink,
			RedirectAttributes redirectAttributes,
			HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");

		if (user == null) {
			return "redirect:/login/loginform";
		}

		// Retrieve cartItems from session
        @SuppressWarnings("unchecked")
        Map<Long, CartItem> cartItemsMap = (Map<Long, CartItem>) session.getAttribute("cartItems");

        if (cartItemsMap == null) {
            cartItemsMap = new HashMap<>();
            session.setAttribute("cartItems", cartItemsMap);
        }

        // Check if the product already exists in the cart
        CartItem existingItem = cartItemsMap.get(productId);
        if (existingItem != null) {
            // Update the quantity if the item exists
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            // Add new item
            CartItem newItem = new CartItem();
            newItem.setProductID(productId);
            newItem.setName(productName);
            newItem.setPrice(price);
            newItem.setQuantity(quantity);
            newItem.setImgLink(imgLink);
            cartItemsMap.put(productId, newItem);
            if ("buy".equals(action)) {
				return "redirect:/shopping-cart/view";
			} else {
				redirectAttributes.addFlashAttribute("message", "Sản phẩm đã được thêm vào giỏ hàng!");
				return "redirect:/shop/" + productId;
			}
        }
		return "404";
	}

	@GetMapping("/delete/{id}")
	public String removeCart(@PathVariable Long id) {
		cartItemService.remove(id);
		return "redirect:/shopping-cart/view";
	}

	// Thanh
	@PostMapping("/submit")
	public String submitCart(@RequestParam("cartItems") String cartItemsJson,
			HttpSession session,
			RedirectAttributes redirectAttributes) {
		
		// Parse the JSON string into a list of cart items
		ObjectMapper objectMapper = new ObjectMapper();
		List<CartItem> selectedItems = new ArrayList<>();

		try {
			// Convert the JSON array into a list of CartItem objects
			selectedItems = Arrays.asList(objectMapper.readValue(cartItemsJson, CartItem[].class));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "redirect:/shopping-cart/view"; // Handle error, redirect back to cart
		}
		
		List<String> errorMessages = new ArrayList<>();
		
		// At this point, you have the selected cart items in 'selectedItems'
        // Now check if the quantity in the database is less than the quantity ordered
        for (CartItem cartItem : selectedItems) {
            Product product = productService.getProductById(cartItem.getProductID()).orElse(null);

            if (product == null) {
                // Handle product not found
                redirectAttributes.addFlashAttribute("errorMessage", "Product not found: " + cartItem.getName());
                continue;
            }

            if (product.getQuantity() < cartItem.getQuantity()) {
                // Insufficient stock, show an error message
            	errorMessages.add("Sản phẩm '" + cartItem.getName() + "' chỉ còn " + product.getQuantity() + " trong kho.");
            }
        }
        
        // If there are any error messages, display them and redirect to the cart page
        if (!errorMessages.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
            return "redirect:/shopping-cart/view"; 
        }

        // If all quantities are valid, proceed with saving selected items to session for checkout
		session.setAttribute("selectedCartItems", selectedItems);

		return "redirect:/checkout"; // Redirect to checkout page
	}

}
