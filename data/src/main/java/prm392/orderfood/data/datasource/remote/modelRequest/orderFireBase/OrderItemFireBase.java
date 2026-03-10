package prm392.orderfood.data.datasource.remote.modelRequest.orderFireBase;

public class OrderItemFireBase {
    private String itemId; // Unique identifier for the item
    private int quantity; // Quantity of the item ordered
    private double price; // Price of the item at the time of order

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
