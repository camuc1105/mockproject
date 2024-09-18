package fashion.mock.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fashion.mock.model.Category;
import fashion.mock.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByProductName(String ProductName);

    Product findByProductName(String ProductName);
    
    List<Product> findByCategory(Category category);
    
    @Query("SELECT c FROM Product c WHERE LOWER(c.productName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Product> searchByName(@Param("searchTerm") String searchTerm, Pageable pageable);

    Page<Product> findAll(Pageable pageable);
}