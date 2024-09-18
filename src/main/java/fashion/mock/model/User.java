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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "USERS")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Email(message = "Email phải đúng định dạng.")
	@NotBlank(message = "Email không được để trống.")
	@Column(nullable = false, unique = true, length = 100)
	private String email;

	@NotBlank(message = "Mật khẩu không được để trống.")
	@Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự.")
	@Pattern(regexp = ".*[A-Z].*", message = "Mật khẩu phải có ít nhất 1 ký tự viết hoa.")
	@Column(nullable = false, length = 100)
	private String password;

	@NotBlank(message = "Tên người dùng không được để trống.")
	@Column(name = "userName", nullable = false, length = 100)
	private String userName;

	@NotBlank(message = "Số điện thoại không được để trống.")
	@Pattern(regexp = "\\d{10}", message = "Số điện thoại phải có 10 chữ số.")
	@Column(nullable = false, length = 10)
	private String phone;

	@NotBlank(message = "Địa chỉ không được để trống.")
	@Column(nullable = false, length = 100)
	private String address;

	@Column(length = 20)
	private String status;

	@Column(name = "createdDate", nullable = false)
	private LocalDate createdDate;

	@Column(name = "updatedDate")
	private LocalDate updatedDate;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<UserRole> userRoles;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Order> orders;

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

	public List<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	
}
