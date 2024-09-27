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
@Table(name = "PAYMENT")
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "paymentMethod", nullable = false, unique = true, columnDefinition = "NVARCHAR(255)")
	private String paymentMethod;

	@Column(columnDefinition = "NVARCHAR(1000)")
	private String description;

	@Column(columnDefinition = "NVARCHAR(255)")
	private String status;

	@Column(name = "createdDate", nullable = false)
	private LocalDate createdDate;

	@OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
	private List<TransactionHistory> transactionHistories;

	public Payment(Long id, String paymentMethod, String description, String status, LocalDate createdDate) {
		super();
		this.id = id;
		this.paymentMethod = paymentMethod;
		this.description = description;
		this.status = status;
		this.createdDate = createdDate;
	}

	public Payment() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

}
