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

@Entity
@Table(name = "DISCOUNT")
public class Discount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "productId", nullable = false)
	private Product product;

	@Column(name = "discountPercent", nullable = false)
	private Double discountPercent;

	@Column(name = "startDate", nullable = false)
	private LocalDate startDate;

	@Column(name = "endDate", nullable = false)
	private LocalDate endDate;

	@Column(name = "createdDate", nullable = false)
	private LocalDate createdDate;

	@Column(name = "updatedDate")
	private LocalDate updatedDate;

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

}
