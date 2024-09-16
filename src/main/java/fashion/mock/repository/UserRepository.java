package fashion.mock.repository;

import fashion.mock.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User getUserByEmail(String email);
    public Boolean  existsByEmail(String email);
}
