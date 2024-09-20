/**
 * Trần Thảo
 */
package fashion.mock.repository;

import fashion.mock.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User findUserByEmail (String email);
    Optional<User>findByPassword(String password);
    Boolean existsByEmail(String email);
    Boolean existsByPassword(String password);
    Optional<User> findByUserName (String userName);
    Boolean existsByUserName(String userName);

}
