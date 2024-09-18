/**
 * Author: Ngô Văn Quốc Thắng 11/05/1996
 */
package fashion.mock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fashion.mock.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByRole(String role);
}