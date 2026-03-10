package prm392.orderfood.domain.models.orders;

import java.util.List;

import prm392.orderfood.domain.models.orderItem.OrderItem;

public class Order {
    private String customerId;
    private String shopId;
    private List<OrderItem> orderItems;
    private String paymentMethod; // COD and Bank
    private double totalAmount;
    private String orderStatus; // e.g., "Pending", "Completed", "Cancelled", "Confirmed", "Delivered"

    public Order() {
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
