package prm392.orderfood.domain.models.orders;

import java.util.List;

import prm392.orderfood.domain.models.orderItem.OrderItemModel;

public class BankingOrderRequest {
    private String customerId;
    private String shopId;
    private List<OrderItemModel> orderItems;
    private String orderStatus;
    private String paymentMethod;
    private double totalAmount;
    private String payosOrderCode;

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

    public List<OrderItemModel> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemModel> orderItems) {
        this.orderItems = orderItems;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
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

    public String getPayosOrderCode() {
        return payosOrderCode;
    }

    public void setPayosOrderCode(String payosOrderCode) {
        this.payosOrderCode = payosOrderCode;
    }
}
