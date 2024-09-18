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
    public User updatePassword(User user, String password) {
        user.setUpdatedDate(LocalDate.now());
        user.setPassword(bCryptPasswordEncoder.encode(password));  // Mã hóa mật khẩu trước khi lưu
        return userRepository.save(user);  // Lưu lại thông tin người dùng sau khi cập nhật mật khẩu
    }
    public Boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }

}
