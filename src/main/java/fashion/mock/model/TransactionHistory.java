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
@Table(name = "TRANSACTION_HISTORY")
public class TransactionHistory {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "paymentId", nullable = false)
    private Payment payment;

    @Column(name = "transactionDate", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "transactionAmount", nullable = false)
    private Double transactionAmount;

    @Column(length = 20)
    private String status;

	public TransactionHistory(Long id, Order order, Payment payment, LocalDate transactionDate,
			Double transactionAmount, String status) {
		super();
		this.id = id;
		this.order = order;
		this.payment = payment;
		this.transactionDate = transactionDate;
		this.transactionAmount = transactionAmount;
		this.status = status;
	}

	public TransactionHistory() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public LocalDate getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    
}
