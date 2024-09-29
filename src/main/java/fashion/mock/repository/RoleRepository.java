/**
 * Trần Thảo
 */
package fashion.mock.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fashion.mock.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findById(Long id);
  /**
   * Author: Ngô Văn Quốc Thắng 11/05/1996
 */
//  	Role findByRole(String role);
}

