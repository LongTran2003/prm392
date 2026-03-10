package prm392.orderfood.domain.models.transactions;

import java.util.Date;

public class Transaction {
    private String id;
    private double amount;
    private String description;
    private String type;
    private String orderCode;
    private String status;
    private Date paymentTime;
    private Date createdAt;
    private String userId;
    private String orderId;

    // Constructor

    public Transaction() {
    }

    public Transaction(String id, double amount, String description, String type, String orderCode, String status, Date paymentTime, Date createdAt, String userId, String orderId) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.orderCode = orderCode;
        this.status = status;
        this.paymentTime = paymentTime;
        this.createdAt = createdAt;
        this.userId = userId;
        this.orderId = orderId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
