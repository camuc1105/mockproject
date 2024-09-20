/**
 * Trần Thảo
 */
package fashion.mock.service;

import fashion.mock.model.User;
import fashion.mock.repository.UserDetailsImpl;
import fashion.mock.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param email the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Tìm người dùng theo email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với email: " + email));

        // Trả về UserDetails (có thể tạo một lớp UserDetailsImpl để thực hiện)
        return new UserDetailsImpl(user);
    }

    public UserDetails loadUserByPassword(String password) throws UsernameNotFoundException {
        // Tìm người dùng theo password
        User user = userRepository.findByPassword(password)
                .orElseThrow(() -> new UsernameNotFoundException("Mật khẩu không đúng"));

        // Trả về UserDetails (có thể tạo một lớp UserDetailsImpl để thực hiện)
        return new UserDetailsImpl(user);
    }
}
