package fashion.mock.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fashion.mock.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
