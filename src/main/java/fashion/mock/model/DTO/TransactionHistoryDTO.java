package fashion.mock.model.DTO;

import java.time.LocalDate;

public class TransactionHistoryDTO {

    private Long orderId;          
    private LocalDate orderDate;     
    private String orderStatus;      
    private LocalDate transactionDate;   
    private Double transactionAmount;    
    private String transactionStatus; 

    public TransactionHistoryDTO(Long orderId, LocalDate orderDate, String orderStatus,
                                 LocalDate transactionDate, Double transactionAmount, String transactionStatus) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.transactionDate = transactionDate;
        this.transactionAmount = transactionAmount;
        this.transactionStatus = transactionStatus;
    }

    // Getters và setters cho các trường
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
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

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
}
