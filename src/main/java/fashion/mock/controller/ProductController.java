/**
 * Author: Nguyen Cong Huan. 06/03/1999
 */
package fashion.mock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fashion.mock.service.CartItemService;
import fashion.mock.service.ProductService;

@Controller
@RequestMapping("/product")
public class ProductController {
	@Autowired
	ProductService service;
	@Autowired
	private CartItemService cartItemService;

	@GetMapping("/view")
	public String viewProducts(Model model) {
		model.addAttribute("products", service.getAllProducts());
		model.addAttribute("totalItems", cartItemService.getCount());
		return "product-view";

	}
}
