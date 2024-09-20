package fashion.mock.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "DISCOUNT")
public class Discount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Phần trăm giảm giá không được để trống")
	@Min(value = 0, message = "Phần trăm giảm giá phải lớn hơn hoặc bằng 0")
	@Max(value = 100, message = "Phần trăm giảm giá phải nhỏ hơn hoặc bằng 100")
	@Column(name = "discount_percent", nullable = false)
	private Double discountPercent;

	@NotNull(message = "Ngày bắt đầu không được để trống")
	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	@NotNull(message = "Ngày kết thúc không được để trống")
	@Column(name = "end_date", nullable = false)
	private LocalDate endDate;

	@Column(name = "created_date", nullable = false)
	private LocalDate createdDate;

	@Column(name = "updated_date")
	private LocalDate updatedDate;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Transient
	private String productName;

	public Discount(Long id, Product product, Double discountPercent, LocalDate startDate, LocalDate endDate,
			LocalDate createdDate, LocalDate updatedDate) {
		super();
		this.id = id;
		this.product = product;
		this.discountPercent = discountPercent;
		this.startDate = startDate;
		this.endDate = endDate;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}

	public Discount() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Double getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(Double discountPercent) {
		this.discountPercent = discountPercent;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

}
