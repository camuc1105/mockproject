/**
 * Author: Lê Nguyên Minh Quý 27/06/1998
 */
package fashion.mock.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import fashion.mock.model.Category;
import fashion.mock.model.Product;
import fashion.mock.service.CategoryService;
import fashion.mock.service.ProductService;

@Controller
public class ProductDetailController {
	@Autowired
	private ProductService productService;

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/shop/{id}")
	public String showProductDetail(@PathVariable Long id, Model model) {
		Product product = productService.getProductById(id)
				.orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm với id: " + id));

		// Lấy danh sách tất cả các danh mục
		List<Category> categories = categoryService.getAllCategories();
		List<Category> aoCategories = categories.stream()
				.filter(category -> category.getCategoryName().startsWith("Áo")).collect(Collectors.toList());
		List<Category> quanCategories = categories.stream()
				.filter(category -> category.getCategoryName().startsWith("Quần")).collect(Collectors.toList());

//        List<Boolean> productsOnDiscount = products.stream()
//            .map(productService::isProductOnDiscount)
//            .collect(Collectors.toList());
//        List<Double> discountedPrices = products.stream()
//            .map(productService::getDiscountedPrice)
//            .collect(Collectors.toList());

		boolean isOnDiscount = productService.isProductOnDiscount(product);
		double discountedPrice = productService.getDiscountedPrice(product);

		model.addAttribute("product", product);
		model.addAttribute("isOnDiscount", isOnDiscount);
		model.addAttribute("discountedPrice", discountedPrice);
		model.addAttribute("aoCategories", aoCategories);
		model.addAttribute("quanCategories", quanCategories);
		model.addAttribute("categories", categories);

		return "shopdetail";
	}
}