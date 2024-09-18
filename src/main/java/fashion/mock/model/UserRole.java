package fashion.mock.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "USERROLE")
public class UserRole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private UserRoleId id;
	@MapsId("userId")
	@ManyToOne
	@JoinColumn(name = "userId", insertable = false, updatable = false)
	private User user;

	@MapsId("roleId")
	@ManyToOne
	@JoinColumn(name = "roleId", insertable = false, updatable = false)
	private Role role;

	public UserRole(User user, Role role) {
		super();
		this.id = new UserRoleId(user.getId(), role.getId());
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
		if (this == o) return true;
		if (!(o instanceof UserRole userRole)) return false;
		return Objects.equals(id, userRole.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
