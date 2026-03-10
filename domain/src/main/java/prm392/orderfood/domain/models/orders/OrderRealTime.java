package prm392.orderfood.domain.models.orders;

import java.util.List;
import java.util.Objects;

import prm392.orderfood.domain.models.orderItem.OrderItemRealTime;

/**
 * Represents a real-time order in the system.
 */
public class OrderRealTime {
    private String firebaseId;
    private String customerId;
    private String shopId;
    private List<OrderItemRealTime> orderItems;
    private String paymentMethod; // COD and Bank
    private double totalAmount;
    private String orderStatus;

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
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

    public List<OrderItemRealTime> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemRealTime> orderItems) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderRealTime)) return false;
        OrderRealTime that = (OrderRealTime) o;
        return Double.compare(that.totalAmount, totalAmount) == 0 &&
                Objects.equals(firebaseId, that.firebaseId) &&
                Objects.equals(customerId, that.customerId) &&
                Objects.equals(shopId, that.shopId) &&
                Objects.equals(orderItems, that.orderItems) &&
                Objects.equals(paymentMethod, that.paymentMethod) &&
                Objects.equals(orderStatus, that.orderStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firebaseId, customerId, shopId, orderItems, paymentMethod, totalAmount, orderStatus);
    }
}
