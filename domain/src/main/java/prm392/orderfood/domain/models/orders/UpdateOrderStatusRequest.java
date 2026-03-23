package prm392.orderfood.domain.models.orders;

public class UpdateOrderStatusRequest {
    private String orderStatus;

    public UpdateOrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
