package fashion.mock.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fashion.mock.model.Category;
import fashion.mock.model.Product;
import fashion.mock.repository.CategoryRepository;
import fashion.mock.repository.ProductRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    public List<Product> getProductsByCategory(String categoryName) {
        Category category = categoryRepository.findByCategoryName(categoryName);
        return productRepository.findByCategory(category);
    }
    public Category getCategoryByName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName);
    }
    
    public Category addCategory(Category category) {
        validateCategory(category);
        category.setCreatedDate(LocalDate.now());
        return categoryRepository.save(category);
    }

    public Category updateCategory(Category category) {
        validateCategory(category);
        Category existingCategory = categoryRepository.findById(category.getId())
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục với ID: " + category.getId()));
        
        existingCategory.setCategoryName(category.getCategoryName().trim());
        existingCategory.setUpdatedDate(LocalDate.now());
        return categoryRepository.save(existingCategory);
    }

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

    // Xóa Category
    public boolean deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
    public Page<Category> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }
    
    // Lấy tất cả Category
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    //dropdow nav
    public List<Category> getCategoriesByPrefix(String prefix) {
        return categoryRepository.findByCategoryNameStartingWith(prefix);
    }

    

    // Lấy Category theo id
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }
    
    // Tìm kiếm
    public Page<Category> searchCategories(String searchTerm, Pageable pageable) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return categoryRepository.findAll(pageable);
        }
        return categoryRepository.searchByName(searchTerm.trim(), pageable);
    }
}

