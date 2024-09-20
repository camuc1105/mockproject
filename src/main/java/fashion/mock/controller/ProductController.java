/**
 * Author: Le Nguyen Minh Quy
 */

package fashion.mock.controller;

import fashion.mock.model.Product;
import fashion.mock.service.ProductService;
import fashion.mock.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private CategoryService categoryService;

	// Thêm phương thức mới để xử lý đường dẫn ảnh
	@ModelAttribute("imagePath")
	public String imagePath() {
		return "/images/";
	}

	@GetMapping
	public String listProducts(Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {
		Page<Product> productPage = productService.getAllProducts(PageRequest.of(page, size));
		model.addAttribute("products", productPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", productPage.getTotalPages());
		model.addAttribute("totalItems", productPage.getTotalElements());
		return "adminlistproduct";
	}

	@GetMapping("/new")
	public String showAddProductForm(Model model) {
		model.addAttribute("product", new Product());
		model.addAttribute("categories", categoryService.getAllCategories());
		return "adminformproduct";
	}

	@GetMapping("/edit/{id}")
	public String showUpdateProductForm(@PathVariable Long id, Model model) {
		Product product = productService.getProductById(id)
				.orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
		model.addAttribute("product", product);
		model.addAttribute("categories", categoryService.getAllCategories());
		return "adminformproduct";
	}

	@PostMapping
	public String addProduct(@ModelAttribute Product product,
			@RequestParam("imageFiles") List<MultipartFile> imageFiles, RedirectAttributes redirectAttributes) {
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
	public String updateProduct(@ModelAttribute Product product,
			@RequestParam("imageFiles") List<MultipartFile> imageFiles, RedirectAttributes redirectAttributes) {
		try {
			productService.updateProduct(product, imageFiles);
			redirectAttributes.addFlashAttribute("successMessage", "Cập nhật sản phẩm thành công!");
			return "redirect:/products";
		} catch (IllegalArgumentException e) {
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
	public String searchProducts(@RequestParam(value = "searchTerm", required = false) String searchTerm,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, Model model) {
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
}

/*
 * @Controller
 * 
 * @RequestMapping("/product") public class ProductController { private final
 * ProductService productService; private final CartItemService cartItemService;
 * 
 * public ProductController(ProductService productService, CartItemService
 * cartItemService) { this.productService = productService; this.cartItemService
 * = cartItemService; }
 * 
 * @GetMapping("/view") public String viewProducts(Model model) {
 * model.addAttribute("products", productService.getAllProducts());
 * model.addAttribute("totalItems", cartItemService.getCount()); return
 * "product-view";
 * 
 * } }
 */
