package fashion.mock.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fashion.mock.model.Discount;
import fashion.mock.model.Product;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

    @Query("SELECT d FROM Discount d WHERE LOWER(d.product.productName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Discount> searchByProductName(@Param("searchTerm") String searchTerm, Pageable pageable);

    Page<Discount> findAll(Pageable pageable);

    List<Discount> findByProductId(Long productId);
    
    @Query("SELECT d FROM Discount d WHERE d.product = :product " +
            "AND ((d.startDate <= :endDate AND d.endDate >= :startDate) OR " +
            "(d.startDate >= :startDate AND d.startDate <= :endDate) OR " +
            "(d.endDate >= :startDate AND d.endDate <= :endDate))")
     List<Discount> findOverlappingDiscounts(@Param("product") Product product, 
                                             @Param("startDate") LocalDate startDate, 
                                             @Param("endDate") LocalDate endDate);

     @Query("SELECT d FROM Discount d WHERE d.product = :product " +
            "AND ((d.startDate <= :endDate AND d.endDate >= :startDate) OR " +
            "(d.startDate >= :startDate AND d.startDate <= :endDate) OR " +
            "(d.endDate >= :startDate AND d.endDate <= :endDate)) " +
            "AND d.id != :excludeId")
     List<Discount> findOverlappingDiscounts(@Param("product") Product product, 
                                             @Param("startDate") LocalDate startDate, 
                                             @Param("endDate") LocalDate endDate, 
                                             @Param("excludeId") Long excludeId);
}