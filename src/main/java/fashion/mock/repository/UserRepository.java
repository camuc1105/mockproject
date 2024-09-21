/**
 * Trần Thảo
 */
package fashion.mock.repository;

import fashion.mock.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User findUserByEmail (String email);
    Optional<User>findByPassword(String password);
    Boolean existsByEmail(String email);
    Boolean existsByPassword(String password);
    Optional<User> findByUserName (String userName);
    Boolean existsByUserName(String userName);
  
  /**
  * Author: Ngô Văn Quốc Thắng 11/05/1996
 * Author: Nguyễn Viết Hoàng Phúc 22/11/1997
 */
public User getUserByEmail(String email);


//	public Boolean existsByEmail(String email);
	

// 	public Boolean existsByEmail(String email);


//	User findByEmail(String email);

	@Query("SELECT u FROM User u WHERE LOWER(u.userName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
	Page<User> searchByNameOrEmail(@Param("searchTerm") String searchTerm, Pageable pageable);

}
