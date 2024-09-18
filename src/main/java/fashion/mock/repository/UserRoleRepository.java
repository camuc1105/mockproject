/**
 * Author: Ngô Văn Quốc Thắng 11/05/1996
 */
package fashion.mock.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import fashion.mock.model.User;
import fashion.mock.model.UserRole;
import fashion.mock.model.UserRoleId;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
	void deleteByUser(User user);
	
	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
	List<UserRole> findByUser(User user);
}