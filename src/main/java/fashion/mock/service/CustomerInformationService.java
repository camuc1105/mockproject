package fashion.mock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fashion.mock.model.User;
import fashion.mock.repository.CustomerInformationRepository;

/**
 * @author Duong Van Luc 01/07/2000
 */

@Service
public class CustomerInformationService {
    
    @Autowired
    private CustomerInformationRepository customerInformationRepository;
    
    public User getUserById(Long id) {
        return customerInformationRepository.findById(id).orElse(null);
    }
    
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = getUserById(userId);

        if (user.getPassword().equals(oldPassword)) {
            user.setPassword(newPassword);
            customerInformationRepository.save(user);
            return true;
        }
        return false;
    }
    
    public void updateUserInfo(User user) {
        customerInformationRepository.save(user);
    }
}
