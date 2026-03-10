package prm392.orderfood.domain.models.payment;

public class PaymentResultRequest {
    private String orderCode;
    private String status;

    public PaymentResultRequest(String orderCode, String status) {
        this.orderCode = orderCode;
        this.status = status;
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
}
