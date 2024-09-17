/**
 * Author: Nguyen Cong Huan. 06/03/1999
 */
package fashion.mock.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fashion.mock.model.CartItem;
import fashion.mock.model.Product;
import fashion.mock.service.CartItemService;
import fashion.mock.service.ProductService;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/shopping-cart")
public class ShoppingCartController {
	@Autowired
	private ProductService productService;
	@Autowired
	private CartItemService cartItemService;

	@GetMapping("/view")
	public String viewCart(Model model,HttpSession session) {	
		  // Lấy giỏ hàng từ session hoặc khởi tạo mới nếu chưa có
		@SuppressWarnings("unchecked")
		Collection<CartItem> cartItems = (Collection<CartItem>) session.getAttribute("cartItems");
        if (cartItems == null) {
            cartItems = cartItemService.getAllItems();     
            session.setAttribute("cartItems", cartItems);
        }
        model.addAttribute("totalItems", cartItemService.getCount());
        model.addAttribute("cartItems", cartItems);
        return "cart-item";  // Trả về trang giỏ hàng
	}

	@GetMapping("/add/{id}")
	public String addCart(@PathVariable long id,Model model) {
		Product product = productService.findProductById(id);
		if (product != null) {
			CartItem item = new CartItem();
			item.setProductID(product.getId());
			item.setName(product.getProductName());
			item.setPrice(product.getPrice());
			item.setQuantity(1);
			cartItemService.add(item);
		}
		
		return "redirect:/shopping-cart/view";
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
