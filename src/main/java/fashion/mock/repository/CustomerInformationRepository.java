/**
 * @author Duong Van Luc 01/07/2000
 */
package fashion.mock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fashion.mock.model.User;

@Repository
public interface CustomerInformationRepository extends JpaRepository<User, Long> {

}
