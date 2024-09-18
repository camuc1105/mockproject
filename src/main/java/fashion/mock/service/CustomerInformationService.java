/**
 * @author Duong Van Luc 01/07/2000
 */
package fashion.mock.service;

import org.springframework.stereotype.Service;

import fashion.mock.model.User;
import fashion.mock.repository.CustomerInformationRepository;

@Service
public class CustomerInformationService {

	private final CustomerInformationRepository customerInformationRepository;

	public CustomerInformationService(CustomerInformationRepository customerInformationRepository) {
		this.customerInformationRepository = customerInformationRepository;
	}

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
