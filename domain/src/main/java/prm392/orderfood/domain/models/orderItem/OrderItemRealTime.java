package prm392.orderfood.domain.models.orderItem;

import java.util.Objects;

public class OrderItemRealTime {
    private String itemId;
    private int quantity;
    private double price;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItemRealTime)) return false;
        OrderItemRealTime that = (OrderItemRealTime) o;
        return quantity == that.quantity &&
                Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, quantity);
    }
}
