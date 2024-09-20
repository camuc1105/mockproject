/**
 * Author: Le Nguyen Minh Quy
 */
package fashion.mock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fashion.mock.model.Product;
import fashion.mock.service.ProductService;
import fashion.mock.service.CategoryService;

@Controller
@RequestMapping("/shop")
public class ProductUserController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String listProducts(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "9") int max,
                               @RequestParam(required = false) String searchTerm,
                               @RequestParam(required = false) String sortBy,
                               @RequestParam(required = false) String color,
                               @RequestParam(required = false) String size,
                               @RequestParam(required = false) String priceRange) {
        
        PageRequest pageRequest = PageRequest.of(page, max);
        Page<Product> productPage = productService.getFilteredProducts(searchTerm, sortBy, color, size, priceRange, pageRequest);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("color", color);
        model.addAttribute("size", size);
        model.addAttribute("priceRange", priceRange);
        model.addAttribute("categories", categoryService.getAllCategories());

        return "shop";
    }
}