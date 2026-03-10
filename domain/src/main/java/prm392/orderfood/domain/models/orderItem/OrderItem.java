package prm392.orderfood.domain.models.orderItem;

import prm392.orderfood.domain.models.menuItem.MenuItemResponse;

public class OrderItem {
    private MenuItemResponse item; // The menu item being ordered
    private int quantity; // Quantity of the item ordered
    private double price; // Price of the item at the time of order

    public OrderItem(MenuItemResponse item, int quantity, double price) {
        this.item = item;
        this.quantity = quantity;
        this.price = price;
    }

    public OrderItem() {
    }

    public MenuItemResponse getItem() {
        return item;
    }

    public void setItem(MenuItemResponse item) {
        this.item = item;
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
