package fashion.mock.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fashion.mock.model.Category;
import fashion.mock.model.User;
import fashion.mock.service.CartItemService;
import fashion.mock.service.CategoryService;
import fashion.mock.service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/home")
public class HomeController {

	private final CategoryService categoryService;
	private CartItemService cartItemService;
	private UserService userService;

	public HomeController(CategoryService categoryService, CartItemService cartItemService, UserService userService) {
		super();
		this.categoryService = categoryService;
		this.cartItemService = cartItemService;
		this.userService = userService;
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
				.filter(category -> category.getCategoryName().startsWith("Quan")).collect(Collectors.toList());
		// Thêm danh sách "Áo" và "Quần" vào model
		model.addAttribute("aoCategories", aoCategories);
		model.addAttribute("quanCategories", quanCategories);

		model.addAttribute("totalCartItems", cartItemService.getCount());

		// thảo
		
		User user = (User) session.getAttribute("user");
		boolean isAdmin = false; // Initialize isAdmin

		if (user != null) {
		    isAdmin = userService.isAdmin(user.getId());
		    model.addAttribute("user", user);
		} else {
		    // Handle the case where user is null (e.g., redirect, set an error message, etc.)
		}
		model.addAttribute("isAdmin", isAdmin);
		return "home";
	}

}
