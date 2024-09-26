/**
 * Author: Nguyen Cong Huan. 06/03/1999
 */
package fashion.mock.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import fashion.mock.model.Product;
import fashion.mock.model.User;
import fashion.mock.service.ProductService;
import fashion.mock.service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/shopping-cart")
public class ShoppingCartController {

	private static final String CART_ITEMS = "cartItems";
	private static final String REDIRECT_CART_VIEW = "redirect:/shopping-cart/view";

	private final UserService userService;
	private final ProductService productService;
	private final ShoppingCartUtils shoppingCartUtils;

	public ShoppingCartController(UserService userService, ProductService productService,
			ShoppingCartUtils shoppingCartUtils) {
		this.userService = userService;
		this.productService = productService;
		this.shoppingCartUtils = shoppingCartUtils;
	}

	@GetMapping("/view")
	public String viewCart(Model model, HttpSession session) {
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
		return "cart-item";
	}

	@PostMapping("/add")
	public String addCart(@RequestParam Long productId, @RequestParam double price,
			@RequestParam int quantity, @RequestParam String action, @RequestParam String imgLink,
			RedirectAttributes redirectAttributes, HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");

		if (user == null) {
			return "redirect:/login/loginform";
		}

		// Retrieve cartItems from session
		@SuppressWarnings("unchecked")
		Map<Long, CartItem> cartItemsMap = (Map<Long, CartItem>) session.getAttribute(CART_ITEMS);
		if (cartItemsMap == null) {
			cartItemsMap = new HashMap<>();
			session.setAttribute(CART_ITEMS, cartItemsMap);
		}

		// Check if the product already exists in the cart
		CartItem existingItem = cartItemsMap.get(productId);
		if (existingItem != null) {
			existingItem.setQuantity(existingItem.getQuantity() + quantity);
		} else {
			Product product = productService.findProductById(productId);
			CartItem newItem = new CartItem();
			newItem.setProductID(productId);
			newItem.setName(product.getProductName());
			newItem.setPrice(price);
			newItem.setImgLink(imgLink);
			newItem.setColor(product.getColor());
			newItem.setSize(product.getSize());
			newItem.setQuantity(quantity);
			
			cartItemsMap.put(productId, newItem);
		}

		redirectAttributes.addFlashAttribute("message", "Sản phẩm đã được thêm vào giỏ hàng!");

		return "buy".equals(action) ? REDIRECT_CART_VIEW : "redirect:/shop/" + productId;
	}

	@GetMapping("/delete/{id}")
	public String removeCart(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
		// Get cart items from session
		@SuppressWarnings("unchecked")
		Map<Long, CartItem> cartItemsMap = (Map<Long, CartItem>) session.getAttribute(CART_ITEMS);

		if (cartItemsMap != null) {
			// Remove the item from the map using the product ID (id)
			cartItemsMap.remove(id);

			// Update the session attribute
			session.setAttribute(CART_ITEMS, cartItemsMap);

			// Add a message confirming the item was removed
			redirectAttributes.addFlashAttribute("message", "Sản phẩm đã được xóa khỏi giỏ hàng!");
		}

		// Redirect back to the cart view
		return REDIRECT_CART_VIEW;
	}

	// Thanh
	@PostMapping("/submit")
	public String submitCart(@RequestParam(CART_ITEMS) String cartItemsJson, HttpSession session,
			RedirectAttributes redirectAttributes) {

		// Parse the JSON string into a list of cart items
		ObjectMapper objectMapper = new ObjectMapper();
		List<CartItem> selectedItems = new ArrayList<>();

		try {
			// Convert the JSON array into a list of CartItem objects
			selectedItems = Arrays.asList(objectMapper.readValue(cartItemsJson, CartItem[].class));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return REDIRECT_CART_VIEW; // Handle error, redirect back to cart
		}
		
//		for (CartItem cartItem : selectedItems) {
//			System.out.println("Mau sac" + cartItem.getColor());
//		}
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
				errorMessages
						.add("Sản phẩm '" + cartItem.getName() + "' chỉ còn " + product.getQuantity() + " trong kho.");
			}
		}

		// If there are any error messages, display them and redirect to the cart page
		if (!errorMessages.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
			return REDIRECT_CART_VIEW;
		}

		// If all quantities are valid, proceed with saving selected items to session
		// for checkout
		session.setAttribute("selectedCartItems", selectedItems);

		return "redirect:/checkout"; // Redirect to checkout page
	}

}
