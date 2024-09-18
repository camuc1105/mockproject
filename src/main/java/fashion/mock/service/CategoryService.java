package fashion.mock.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fashion.mock.model.Category;
import fashion.mock.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

//    // Thêm mới Category
//    public Category addCategory(Category category) {
//        category.setCreatedDate(LocalDate.now());  // Ngày tạo được thiết lập khi thêm mới
//        return categoryRepository.save(category);
//    }
//    public Category addCategory(Category category) {
//        if (category.getCategoryName() == null || category.getCategoryName().trim().isEmpty()) {
//            throw new IllegalArgumentException("Tên danh mục không được để trống");
//        }
//        if (categoryRepository.existsByCategoryName(category.getCategoryName())) {
//            throw new RuntimeException("Tên danh mục đã tồn tại");
//        }
//        category.setCreatedDate(LocalDate.now());
//        return categoryRepository.save(category);
//    }
//
// // Cập nhật Category
//    public boolean updateCategory(Long id, Category updatedCategory) {
//        Optional<Category> optionalCategory = categoryRepository.findById(id);
//        if (optionalCategory.isPresent()) {
//            Category existingCategory = optionalCategory.get();
//            existingCategory.setCategoryName(updatedCategory.getCategoryName());
//            existingCategory.setUpdatedDate(LocalDate.now());
//            categoryRepository.save(existingCategory);
//            return true;
//        } else {
//            return false;
//        }
//    }
    
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

    // Lấy tất cả Category
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    public Page<Category> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    // Lấy Category theo id
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }
    
    // Tìm kiếm
//    public List<Category> searchCategories(String searchTerm) {
//        if (searchTerm == null || searchTerm.trim().isEmpty()) {
//            return categoryRepository.findAll();
//        }
//        return categoryRepository.searchByName(searchTerm.trim());
//    }
    public Page<Category> searchCategories(String searchTerm, Pageable pageable) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return categoryRepository.findAll(pageable);
        }
        return categoryRepository.searchByName(searchTerm.trim(), pageable);
    }
}

