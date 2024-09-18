/**
 * Author: Ngô Văn Quốc Thắng 11/05/1996
 */
package fashion.mock.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import fashion.mock.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByEmail(String email);

	User findByEmail(String email);

	@Query("SELECT u FROM User u WHERE LOWER(u.userName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
	Page<User> searchByNameOrEmail(@Param("searchTerm") String searchTerm, Pageable pageable);
}