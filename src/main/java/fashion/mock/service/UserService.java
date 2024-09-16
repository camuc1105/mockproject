package fashion.mock.service;


import fashion.mock.model.User;
import fashion.mock.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService() {
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public User createUser(User user) {
        String encoderPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setStatus("unActive");
        user.setCreatedDate(LocalDate.now());
        user.setPassword(encoderPassword);
        return userRepository.save(user);
    }
    public User getByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }
    public User activeUser(User user) {
        user.setStatus("Active");
        return userRepository.save(user);
    }
    public User updatePassword(String email, String newPassword) {
        User user = userRepository.getUserByEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        return userRepository.save(user);
    }
    public Boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }

}
