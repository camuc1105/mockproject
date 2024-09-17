package fashion.mock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fashion.mock.model.User;

/**
 * @author Duong Van Luc 01/07/2000
 */


@Repository
public interface CustomerInformationRepository extends JpaRepository<User, Long> {
    
}
