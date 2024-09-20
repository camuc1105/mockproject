package fashion.mock.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import fashion.mock.model.Discount;

import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

    @Query("SELECT d FROM Discount d WHERE LOWER(d.product.productName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Discount> searchByProductName(@Param("searchTerm") String searchTerm, Pageable pageable);

    Page<Discount> findAll(Pageable pageable);

    List<Discount> findByProductId(Long productId);
}