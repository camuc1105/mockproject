/**
 * Author: Ngô Văn Quốc Thắng 11/05/1996
 */
package fashion.mock.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fashion.mock.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	boolean existsByCategoryName(String categoryName);

	Category findByCategoryName(String categoryName);

	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
	@Query("SELECT c FROM Category c WHERE LOWER(c.categoryName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
	Page<Category> searchByName(@Param("searchTerm") String searchTerm, Pageable pageable);

	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
	Page<Category> findAll(Pageable pageable);

	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
	@Query("SELECT c FROM Category c WHERE c.categoryName LIKE :prefix%")
	List<Category> findByCategoryNameStartingWith(@Param("prefix") String prefix);
}