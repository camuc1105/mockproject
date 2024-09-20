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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "CATEGORY")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Tên danh mục không được để trống")
	@Size(max = 25, message = "Tên danh mục không được dài quá 25 ký tự")
	@Column(name = "categoryName", nullable = false, unique = true, length = 25)
	private String categoryName;

	@Column(name = "createdDate", nullable = false)
	private LocalDate createdDate;

	@Column(name = "updatedDate")
	private LocalDate updatedDate;

	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private List<Product> products;

	public Category(Long id, String categoryName, LocalDate createdDate, LocalDate updatedDate) {
		super();
		this.id = id;
		this.categoryName = categoryName;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}

	public Category() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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
