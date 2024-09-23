/**
 * Author: Nguyen Cong Huan. 06/03/1999
 */
package fashion.mock.controller;

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

import fashion.mock.model.CartItem;
import fashion.mock.model.Category;
import fashion.mock.model.Product;
import fashion.mock.service.CartItemService;
import fashion.mock.service.CategoryService;
import fashion.mock.service.ProductService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/shopping-cart")
public class ShoppingCartController {
	private final ProductService productService;
	private final CartItemService cartItemService;
	private CategoryService categoryService;

	public ShoppingCartController(ProductService productService, CartItemService cartItemService,
			CategoryService categoryService) {
		super();
		this.productService = productService;
		this.cartItemService = cartItemService;
		this.categoryService = categoryService;
	}

	@GetMapping("/view")
	public String viewCart(Model model, HttpSession session) {
		// Lấy tất cả danh mục
		List<Category> categories = categoryService.getAllCategories();
		// Phân loại danh mục dựa trên tên bắt đầu bằng "Áo" hoặc "Quần"
		List<Category> aoCategories = categories.stream()
				.filter(category -> category.getCategoryName().startsWith("Áo")).collect(Collectors.toList());
		List<Category> quanCategories = categories.stream()
				.filter(category -> category.getCategoryName().startsWith("Quan")).collect(Collectors.toList());
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
		return "cart-item"; // Trả về trang giỏ hàng
	}

	@PostMapping("/add")
	public String addCart(@RequestParam Long productId, @RequestParam String productName, @RequestParam double price,
			@RequestParam int quantity, @RequestParam String action, RedirectAttributes redirectAttributes) {

		Product product = productService.findProductById(productId);

		if (product != null) {
			CartItem item = new CartItem();
			item.setProductID(product.getId());
			item.setName(productName);
			item.setPrice(price);
			item.setQuantity(quantity);
			cartItemService.add(item);
			if ("buy".equals(action)) {
				return "redirect:/shopping-cart/view"; // Chuyển hướng đến trang giỏ hàng nếu chọn "Mua ngay"
			} else {
				redirectAttributes.addFlashAttribute("message", "Sản phẩm đã được thêm vào giỏ hàng!");
				return "redirect:/shop/" + productId; // Chuyển hướng về trang hiện tại
			}
		}
//		return "redirect:/shopping-cart/view";
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

}
