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
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/home")
public class HomeController {

	private final CategoryService categoryService;
	private CartItemService cartItemService;

	public HomeController(CategoryService categoryService, CartItemService cartItemService) {
		super();
		this.categoryService = categoryService;
		this.cartItemService = cartItemService;
	}

	/*
	 * @GetMapping public String home(Model model, HttpSession session) { User user
	 * = (User) session.getAttribute("user"); model.addAttribute("user", user); //
	 * this.setUser(user); return "home"; }
	 */

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
		model.addAttribute("user", user);
		return "home";
	}

}
