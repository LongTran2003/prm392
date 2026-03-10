package prm392.orderfood.data.datasource.remote.modelRequest.transaction;

public class WithdrawRequest {
    public String userId;
    public double amount;
    public String description;

    public WithdrawRequest() {
    }

    public WithdrawRequest(String userId, double amount, String description) {
        this.userId = userId;
        this.amount = amount;
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
}
