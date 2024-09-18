	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
package fashion.mock.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import fashion.mock.model.Category;
import fashion.mock.model.Product;
import fashion.mock.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    
//    @Autowired
//    private ProductRepository productRepository;
    
	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
//    public List<Product> getProductsByCategory(String categoryName) {
//        Category category = categoryRepository.findByCategoryName(categoryName);
//        return productRepository.findByCategory(category);
//    }
    
	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
    public Category getCategoryByName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName);
    }
    
	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
    public Category addCategory(Category category) {
        validateCategory(category);
        category.setCreatedDate(LocalDate.now());
        return categoryRepository.save(category);
    }

	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
    public Category updateCategory(Category category) {
        validateCategory(category);
        Category existingCategory = categoryRepository.findById(category.getId())
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục với ID: " + category.getId()));        
        existingCategory.setCategoryName(category.getCategoryName().trim());
        existingCategory.setUpdatedDate(LocalDate.now());
        return categoryRepository.save(existingCategory);
    }

	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
    private void validateCategory(Category category) {
        if (category.getCategoryName() == null || category.getCategoryName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên danh mục không được để trống");
        }
        String trimmedName = category.getCategoryName().trim();
        Category existingCategory = categoryRepository.findByCategoryName(trimmedName);
        if (existingCategory != null && !existingCategory.getId().equals(category.getId())) {
            throw new IllegalArgumentException("Tên danh mục đã tồn tại");
        }
        category.setCategoryName(trimmedName);
    }

	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
    public boolean deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
    
	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
    public Page<Category> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }
    
	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
    //dropdow nav
    public List<Category> getCategoriesByPrefix(String prefix) {
        return categoryRepository.findByCategoryNameStartingWith(prefix);
    }

	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }
    
	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
    public Page<Category> searchCategories(String searchTerm, Pageable pageable) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return categoryRepository.findAll(pageable);
        }
        return categoryRepository.searchByName(searchTerm.trim(), pageable);
    }
}

