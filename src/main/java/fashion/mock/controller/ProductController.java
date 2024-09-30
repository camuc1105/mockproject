/**
 * Author: Lê Nguyên Minh Quý 27/06/1998
 */

package fashion.mock.controller;

import fashion.mock.model.Product;
import fashion.mock.model.User;
import fashion.mock.service.ProductService;
import fashion.mock.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import fashion.mock.service.CartItemService;
import fashion.mock.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CartItemService cartItemService;
	@Autowired
	private UserService userService;

	private boolean checkAdminAccess(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
		User user = (User) session.getAttribute("user");
		if (user == null || !userService.isAdmin(user.getId())) {
			redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền truy cập trang này.");
			return false;
		}
		model.addAttribute("user", user);
		model.addAttribute("isAdmin", true);
		return true;
	}
	// Added new method to handle image paths
	@ModelAttribute("imagePath")
	public String imagePath() {
		return "/images/";
	}

	@GetMapping
	public String listProducts(Model model, HttpSession session, RedirectAttributes redirectAttributes,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
		Page<Product> productPage = productService.getAllProducts(PageRequest.of(page, size));
		if (!checkAdminAccess(session, model, redirectAttributes)) {
			 return "403";
        } 
		model.addAttribute("products", productPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", productPage.getTotalPages());
		model.addAttribute("totalItems", productPage.getTotalElements());
		return "adminlistproduct";
	}

	@GetMapping("/new")
	public String showAddProductForm(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		if (!checkAdminAccess(session, model, redirectAttributes)) {
			 return "403";
        } 
		model.addAttribute("product", new Product());
		model.addAttribute("categories", categoryService.getAllCategories());
		return "adminformproduct";
	}

	@GetMapping("/edit/{id}")
	public String showUpdateProductForm(@PathVariable Long id, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		if (!checkAdminAccess(session, model, redirectAttributes)) {
			 return "403";
        } 
		Product product = productService.getProductById(id)
				.orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
		model.addAttribute("product", product);
		model.addAttribute("categories", categoryService.getAllCategories());
		return "adminformproduct";
	}

	@PostMapping
	public String addProduct(@Valid @ModelAttribute Product product, BindingResult bindingResult,
			@RequestParam("imageFiles") List<MultipartFile> imageFiles, RedirectAttributes redirectAttributes,
			Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("categories", categoryService.getAllCategories());
			return "adminformproduct";
		}
		try {
			productService.addProduct(product, imageFiles);
			redirectAttributes.addFlashAttribute("successMessage", "Thêm sản phẩm thành công!");
			return "redirect:/products";
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			redirectAttributes.addFlashAttribute("product", product);
			return "redirect:/products/new";
		}
	}

	@PostMapping("/update")
	public String updateProduct(@Valid @ModelAttribute Product product, BindingResult bindingResult,
			@RequestParam("imageFiles") List<MultipartFile> imageFiles,
			@RequestParam(value = "deletedImageIds", required = false) String deletedImageIdsJson,
			RedirectAttributes redirectAttributes, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("categories", categoryService.getAllCategories());
			return "adminformproduct";
		}

		try {
			List<Long> deletedImageIds = new ArrayList<>();
			if (deletedImageIdsJson != null && !deletedImageIdsJson.isEmpty()) {
				ObjectMapper objectMapper = new ObjectMapper();
				deletedImageIds = objectMapper.readValue(deletedImageIdsJson, new TypeReference<List<Long>>() {
				});
			}

			productService.updateProduct(product, imageFiles, deletedImageIds);
			redirectAttributes.addFlashAttribute("successMessage", "Cập nhật sản phẩm thành công!");
			return "redirect:/products";
		} catch (IllegalArgumentException | JsonProcessingException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			redirectAttributes.addFlashAttribute("product", product);
			return "redirect:/products/edit/" + product.getId();
		}
	}

	@GetMapping("/delete/{id}")
	public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		boolean deleted = productService.deleteProduct(id);
		if (!deleted) {
			redirectAttributes.addFlashAttribute("message", "Sản phẩm không tồn tại");
			redirectAttributes.addFlashAttribute("messageType", "error");
		} else {
			redirectAttributes.addFlashAttribute("message", "Sản phẩm đã được xóa thành công");
			redirectAttributes.addFlashAttribute("messageType", "success");
		}
		return "redirect:/products";
	}

	@GetMapping("/search")
	public String searchProducts(@RequestParam(required = false) String searchTerm,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, Model model,HttpSession session, RedirectAttributes redirectAttributes) {
	    if (!checkAdminAccess(session, model, redirectAttributes)) {
            return "403";
	    }
	    if (page < 0) {
			page = 0;
		}

		Page<Product> productPage = productService.searchProducts(searchTerm, PageRequest.of(page, size));
		model.addAttribute("products", productPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", productPage.getTotalPages());
		model.addAttribute("totalItems", productPage.getTotalElements());
		model.addAttribute("searchTerm", searchTerm);
		return "adminlistproduct";
	}

	@PostMapping("/delete-image/{imageId}")
	@ResponseBody
	public String deleteImage(@PathVariable Long imageId) {
		boolean deleted = productService.deleteImage(imageId);
		return "{\"success\":" + deleted + "}";
	}

	@GetMapping("/view")
	public String viewProducts(Model model) {
		model.addAttribute("products", productService.getAllProducts());
		model.addAttribute("totalCartItems", cartItemService.getCount());
		return "product-view";
	}
}
