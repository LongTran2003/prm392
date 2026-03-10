package prm392.orderfood.domain.models.category;

import java.util.List;

import prm392.orderfood.domain.models.menuItem.MenuItem;

public class Category {
    private String name = "";
    private String description = "";
    private List<MenuItem> menuItems;

    public Category(String name, String description, List<MenuItem> menuItems) {
        this.name = name;
        this.description = description;
        this.menuItems = menuItems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }
}
