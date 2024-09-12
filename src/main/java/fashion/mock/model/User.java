package fashion.mock.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "USERS")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 100)
	private String email;

	@Column(nullable = false, length = 100)
	private String password;

	@Column(name = "userName", nullable = false, length = 100)
	private String userName;

	@Column(nullable = false, length = 100)
	private String phone;

	@Column(nullable = false, length = 100)
	private String address;

	@Column(length = 20)
	private String status;

	@Column(name = "createdDate", nullable = false)
	private LocalDate createdDate;

	@Column(name = "updatedDate")
	private LocalDate updatedDate;

	// OneToMany Relationship with UserRole
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<UserRole> userRoles;

	// OneToMany Relationship with Order
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Order> orders;

	// OneToMany Relationship with Product
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Product> products;

	public User(Long id, String email, String password, String userName, String phone, String address, String status,
			LocalDate createdDate, LocalDate updatedDate) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.userName = userName;
		this.phone = phone;
		this.address = address;
		this.status = status;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}

	public User() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDate getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDate getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDate updatedDate) {
		this.updatedDate = updatedDate;
	}

}
