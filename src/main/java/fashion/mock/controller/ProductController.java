/**
 * Author: Nguyen Cong Huan. 06/03/1999
 */
package fashion.mock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fashion.mock.service.CartItemService;
import fashion.mock.service.ProductService;

@Controller
@RequestMapping("/product")
public class ProductController {
	private  final ProductService productService;
	private  final CartItemService cartItemService;

	public ProductController(ProductService productService, CartItemService cartItemService) {
		this.productService = productService;
		this.cartItemService = cartItemService;
	}

	@GetMapping("/view")
	public String viewProducts(Model model) {
		model.addAttribute("products", productService.getAllProducts());
		model.addAttribute("totalItems", cartItemService.getCount());
		return "product-view";

	}
}
