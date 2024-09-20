/**
 * Trần Thảo
 */
package fashion.mock.service;

import fashion.mock.model.User;
import fashion.mock.repository.UserRepository;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public boolean getEmail (String email){
        return userRepository.existsByEmail(email);
    }
    public boolean getPassword (String password)  {
        return userRepository.existsByPassword(password);
    }
    public User getStatus(String email){
        return userRepository.findUserByEmail(email);
    }
}
