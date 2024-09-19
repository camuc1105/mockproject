/**
 * Trần Thảo
 */
package fashion.mock.service;

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
        if(!userRepository.existsByEmail(email)){
            throw new UsernameNotFoundException("Không tìm thấy người dùng với email: " + email);
        }
       return true;
    }
    public boolean getPassword (String password) throws AuthenticationException {
        if(!userRepository.existsByPassword(password)){
            throw new AuthenticationException("Sai mật khẩu");
        }
        return true;
    }
}
