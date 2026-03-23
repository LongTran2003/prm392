package prm392.orderfood.data.datasource.remote.modelResponse;

import java.util.List;

public class OrderResponseDto {
    private String orderId;
    private String firebaseOrderId;
    private String shopId;
    private String shopName;
    private String customerName;
    private String orderStatus;
    private String paymentMethod;
    private String paymentStatus;
    private double totalAmount;
    private List<OrderItemResponseDto> orderItems;

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getFirebaseOrderId() { return firebaseOrderId; }
    public void setFirebaseOrderId(String firebaseOrderId) { this.firebaseOrderId = firebaseOrderId; }

    public String getShopId() { return shopId; }
    public void setShopId(String shopId) { this.shopId = shopId; }

    public String getShopName() { return shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public List<OrderItemResponseDto> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItemResponseDto> orderItems) { this.orderItems = orderItems; }

    public static class OrderItemResponseDto {
        private String menuItemId;
        private String menuItemName;
        private int quantity;
        private double price;
        private double subTotal;

        public String getMenuItemId() { return menuItemId; }
        public void setMenuItemId(String menuItemId) { this.menuItemId = menuItemId; }

        public String getMenuItemName() { return menuItemName; }
        public void setMenuItemName(String menuItemName) { this.menuItemName = menuItemName; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }

        public double getSubTotal() { return subTotal; }
        public void setSubTotal(double subTotal) { this.subTotal = subTotal; }
    }
}
