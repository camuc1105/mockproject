/**
 * Author: Nguyen Cong Huan. 06/03/1999
 */
package fashion.mock.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fashion.mock.model.Product;
import fashion.mock.repository.ProductRepository;

@Service
public class ProductService {
	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	List<Product> ls = new ArrayList<Product>();

	public List<Product> getAllProducts() {
		ls = productRepository.findAll();
		return ls;
	}

	public Product findProductById(Long id) {
		for (Product product : ls) {
			if (product.getId() == id) {
				return product;
			}
		}
		return null;
	}
}
