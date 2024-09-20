/**
 * Author: Le Nguyen Minh Quy
 */
package fashion.mock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import fashion.mock.model.Product;
import fashion.mock.service.ProductService;

@Controller
public class ProductDetailController {

    @Autowired
    private ProductService productService;

    @GetMapping("/shop/{id}")
    public String showProductDetail(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm với id: " + id));
        model.addAttribute("product", product);
        return "shopdetail";
    }
}