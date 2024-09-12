package fashion.mock.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "USERROLE")
public class UserRole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne
	@JoinColumn(name = "userId", insertable = false, updatable = false)
	private User user;

	@Id
	@ManyToOne
	@JoinColumn(name = "roleId", insertable = false, updatable = false)
	private Role role;

	public UserRole(User user, Role role) {
		super();
		this.user = user;
		this.role = role;
	}

	public UserRole() {
		super();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		UserRole userRole = (UserRole) o;
		return Objects.equals(user, userRole.user) && Objects.equals(role, userRole.role);
	}

	@Override
	public int hashCode() {
		return Objects.hash(user, role);
	}
}
