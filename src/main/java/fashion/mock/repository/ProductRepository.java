/**
 * Author: Lê Nguyên Minh Quý 27/06/1998
 */
package fashion.mock.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fashion.mock.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    boolean existsByProductName(String ProductName);

    Product findByProductName(String ProductName);
    
    List<Product> findByCategory_CategoryName(String categoryName);

    @Query("SELECT c FROM Product c WHERE LOWER(c.productName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Product> searchByName(@Param("searchTerm") String searchTerm, Pageable pageable);

    Page<Product> findAll(Pageable pageable);
    
    
    Page<Product> findByProductNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Product> findAllByOrderByPriceAsc(Pageable pageable);
    Page<Product> findAllByOrderByPriceDesc(Pageable pageable);
    Page<Product> findByProductNameContainingIgnoreCaseOrderByPriceAsc(String ProductName, Pageable pageable);
    Page<Product> findByProductNameContainingIgnoreCaseOrderByPriceDesc(String nProductNameame, Pageable pageable);
}

