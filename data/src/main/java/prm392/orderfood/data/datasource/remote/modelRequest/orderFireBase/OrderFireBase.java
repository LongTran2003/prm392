package prm392.orderfood.data.datasource.remote.modelRequest.orderFireBase;

import java.util.List;

/**
 * Represents a request to create an order in Firebase.
 * This class is used to structure the data sent to Firebase when creating a new order.
 */
public class OrderFireBase {
    private String customerId;
    private String shopId;
    private List<OrderItemFireBase> orderItems;
    private String paymentMethod; // COD and Bank
    private double totalAmount;
    private String orderStatus;

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

    public List<OrderItemFireBase> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemFireBase> orderItems) {
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
