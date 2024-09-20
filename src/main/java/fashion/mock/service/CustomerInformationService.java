/**
 * @author Duong Van Luc 01/07/2000
 */
package fashion.mock.service;

import java.time.LocalDate;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import fashion.mock.model.User;
import fashion.mock.repository.CustomerInformationRepository;

@Service
public class CustomerInformationService {
    
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final CustomerInformationRepository customerInformationRepository;

	public CustomerInformationService(CustomerInformationRepository customerInformationRepository) {
		this.customerInformationRepository = customerInformationRepository;
		this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
	}

	public User getUserById(Long id) {
		return customerInformationRepository.findById(id).orElse(null);
	}

	public boolean changePassword(Long userId, String oldPassword, String newPassword) {
		User user = getUserById(userId);
		boolean matchPassword = bCryptPasswordEncoder.matches(oldPassword, user.getPassword());
		
		if (matchPassword == true) {
            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
            user.setUpdatedDate(LocalDate.now());
            customerInformationRepository.save(user);
            return true;
        }
		return false;
	}

	public void updateUserInfo(User user) {
		customerInformationRepository.save(user);
	}
}
