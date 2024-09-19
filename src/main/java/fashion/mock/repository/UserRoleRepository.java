/**
 * Trần Thảo
 */
package fashion.mock.repository;

import fashion.mock.model.Role;
import fashion.mock.model.User;
import fashion.mock.model.UserRole;
import fashion.mock.model.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    List<UserRole> findByUser(User user);
    List<UserRole> findByRole(Role role);
}
