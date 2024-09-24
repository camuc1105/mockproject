/**
 * Author: Nguyen Cong Huan. 06/03/1999
 */
package fashion.mock.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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

	private final ProductService productService;
	private final CartItemService cartItemService;
	private CategoryService categoryService;
	private UserService userService;

	public ShoppingCartController(ProductService productService, CartItemService cartItemService,
			CategoryService categoryService, UserService userService) {
		super();
		this.productService = productService;
		this.cartItemService = cartItemService;
		this.categoryService = categoryService;
		this.userService = userService;
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
		Collection<CartItem> cartItems = (Collection<CartItem>) session.getAttribute("cartItems");

		if (cartItems == null) {
			cartItems = cartItemService.getAllItems();
			session.setAttribute("cartItems", cartItems);
		}
		model.addAttribute("totalCartItems", cartItemService.getCount());
		model.addAttribute("cartItems", cartItems);

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
	public String addCart(@RequestParam Long productId, @RequestParam String productName, @RequestParam double price,
			@RequestParam int quantity, @RequestParam String action, RedirectAttributes redirectAttributes,
			HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");

		if (user == null) {
			return "redirect:/login/loginform";
		}
		Product product = productService.findProductById(productId);

		if (product != null) {
			CartItem item = new CartItem();
			item.setProductID(product.getId());
			item.setName(productName);
			item.setPrice(price);
			item.setQuantity(quantity);
			cartItemService.add(item);
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

	@PostMapping("/update")
	public String update(@RequestParam Long id, @RequestParam Integer quantity, Model model) {
		if (quantity < 1) {
			return "redirect:/shopping-cart/view";
		}
		cartItemService.update(id, quantity);
		return "redirect:/shopping-cart/view";
	}

	// Thanh
	@PostMapping("/submit")
	public String submitCart(@RequestParam("cartItems") String cartItemsJson, HttpSession session) {
		// Parse the JSON string into a list of cart items
		ObjectMapper objectMapper = new ObjectMapper(); // Jackson library for JSON parsing
		List<CartItem> selectedItems = new ArrayList<>();

		System.out.println(cartItemsJson);

		try {
			// Convert the JSON array into a list of CartItem objects
			selectedItems = Arrays.asList(objectMapper.readValue(cartItemsJson, CartItem[].class));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "redirect:/shopping-cart/view"; // Handle error, redirect back to cart
		}

		// At this point, you have the selected cart items in 'selectedItems'
		// Save the selected items in session for use in the checkout page
		session.setAttribute("selectedCartItems", selectedItems);

		for (CartItem cartItem : selectedItems) {
			System.out.println(cartItem.toString());
		}

		return "redirect:/checkout"; // Redirect to checkout page
	}

}
