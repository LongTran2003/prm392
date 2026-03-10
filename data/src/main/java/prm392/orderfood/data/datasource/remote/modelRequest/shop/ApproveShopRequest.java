package prm392.orderfood.data.datasource.remote.modelRequest.shop;

public class ApproveShopRequest {
    public String shopId;
    public boolean isApproved;

    public ApproveShopRequest() {
    }

    public ApproveShopRequest(String shopId, boolean isApproved) {
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }
}
