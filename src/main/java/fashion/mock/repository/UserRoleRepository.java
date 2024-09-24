/**
 * Trần Thảo
 */
package fashion.mock.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fashion.mock.model.Role;
import fashion.mock.model.User;
import fashion.mock.model.UserRole;
import fashion.mock.model.UserRoleId;
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    List<UserRole> findByRole(Role role);
  /**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
  void deleteByUser(User user);
	List<UserRole> findByUser(User user);
	List<UserRole> findByUserId (Long userId);
}

