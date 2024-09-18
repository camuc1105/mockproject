/**
 * Author: Ngô Văn Quốc Thắng 11/05/1996
 * Author: Nguyễn Viết Hoàng Phúc 22/11/1997
 */
package fashion.mock.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fashion.mock.model.Role;
import fashion.mock.model.User;
import fashion.mock.model.UserRole;
import fashion.mock.repository.RoleRepository;
import fashion.mock.repository.UserRepository;
import fashion.mock.repository.UserRoleRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final UserRoleRepository userRoleRepository;
	private final PasswordEncoder passwordEncoder;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public UserService(UserRepository userRepository, RoleRepository roleRepository,
			UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.userRoleRepository = userRoleRepository;
		this.passwordEncoder = passwordEncoder;
		this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
	}

	/**
	 * Author: Nguyễn Viết Hoàng Phúc 22/11/1997
	 */
	public User createUser(User user) {
		String encoderPassword = bCryptPasswordEncoder.encode(user.getPassword());
		user.setStatus("unActive");
		user.setCreatedDate(LocalDate.now());
		user.setPassword(encoderPassword);
		return userRepository.save(user);
	}
	
	/**
	 * Author: Nguyễn Viết Hoàng Phúc 22/11/1997
	 */
	public User getByEmail(String email) {
		return userRepository.getUserByEmail(email);
	}
	
	/**
	 * Author: Nguyễn Viết Hoàng Phúc 22/11/1997
	 */
	public User activeUser(User user) {
		user.setStatus("Active");
		return userRepository.save(user);
	}

	/**
	 * Author: Nguyễn Viết Hoàng Phúc 22/11/1997
	 */
	public User updatePassword(User user, String password) {
		user.setUpdatedDate(LocalDate.now());
		user.setPassword(bCryptPasswordEncoder.encode(password)); // Mã hóa mật khẩu trước khi lưu
		return userRepository.save(user); // Lưu lại thông tin người dùng sau khi cập nhật mật khẩu
	}
	
	/**
	 * Author: Nguyễn Viết Hoàng Phúc 22/11/1997
	 */
	public Boolean checkEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
	@Transactional
	public User addUserWithRoles(User user, List<Long> roleIds) {
		validateUser(user);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setCreatedDate(LocalDate.now());
		User savedUser = userRepository.save(user);

		List<Role> roles = roleRepository.findAllById(roleIds);
		List<UserRole> userRoles = roles.stream().map(role -> new UserRole(savedUser, role))
				.collect(Collectors.toList());
		userRoleRepository.saveAll(userRoles);

		return savedUser;
	}

	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
	@Transactional
	public User updateUserWithRoles(User user, List<Long> roleIds) {
		User existingUser = userRepository.findById(user.getId())
				.orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng với ID: " + user.getId()));
		validateUserUpdate(user, existingUser);
		existingUser.setEmail(user.getEmail().trim());
		existingUser.setUserName(user.getUserName().trim());
		existingUser.setPhone(user.getPhone());
		existingUser.setAddress(user.getAddress());
		existingUser.setStatus(user.getStatus());
		existingUser.setUpdatedDate(LocalDate.now());
		if (user.getPassword() != null && !user.getPassword().isEmpty()) {
			existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
		}

		userRoleRepository.deleteByUser(existingUser);
		List<Role> roles = roleRepository.findAllById(roleIds);
		List<UserRole> userRoles = roles.stream().map(role -> new UserRole(existingUser, role))
				.collect(Collectors.toList());
		userRoleRepository.saveAll(userRoles);

		return userRepository.save(existingUser);
	}

	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
	private void validateUser(User user) {
		if (user.getUserName() == null || user.getUserName().trim().isEmpty()) {
			throw new IllegalArgumentException("Tên người dùng không được để trống");
		}
		if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
			throw new IllegalArgumentException("Mật khẩu không được để trống");
		}
		if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
			throw new IllegalArgumentException("Email không được để trống");
		}

		String trimmedEmail = user.getEmail().trim();
		if (userRepository.findByEmail(trimmedEmail) != null) {
			throw new IllegalArgumentException("Email đã tồn tại");
		}
		user.setEmail(trimmedEmail);
	}

	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
	private void validateUserUpdate(User user, User existingUser) {
		if (user.getUserName() == null || user.getUserName().trim().isEmpty()) {
			throw new IllegalArgumentException("Tên người dùng không được để trống");
		}
		if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
			throw new IllegalArgumentException("Email không được để trống");
		}

		String trimmedEmail = user.getEmail().trim();
		User userWithSameEmail = userRepository.findByEmail(trimmedEmail);
		if (userWithSameEmail != null && !userWithSameEmail.getId().equals(existingUser.getId())) {
			throw new IllegalArgumentException("Email đã tồn tại");
		}
	}

	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
	public Page<User> getAllUsers(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
	public Optional<User> getUserById(Long id) {
		return userRepository.findById(id);
	}

	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
	public Page<User> searchUsers(String searchTerm, Pageable pageable) {
		if (searchTerm == null || searchTerm.trim().isEmpty()) {
			return userRepository.findAll(pageable);
		}
		return userRepository.searchByNameOrEmail(searchTerm.trim(), pageable);
	}

	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
	public boolean deleteUser(Long id) {
		if (userRepository.existsById(id)) {
			userRepository.deleteById(id);
			return true;
		} else {
			return false;
		}
	}
}
