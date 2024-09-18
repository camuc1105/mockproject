	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
package fashion.mock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fashion.mock.model.Role;
import fashion.mock.repository.RoleRepository;
import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
    public Role getRoleByName(String roleName) {
        return roleRepository.findByRole(roleName);
    }

	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}