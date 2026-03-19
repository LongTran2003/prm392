package prm392.orderfood.data.datasource.remote.modelResponse.category;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import prm392.orderfood.data.datasource.remote.modelResponse.menuItem.GetMenuItemResponse;

public class GetCategoriesInShopMenu {
    // FIX-BE-MAP: categoryId/categoryName từ BE
    @SerializedName("categoryId")
    private String id;

    @SerializedName("categoryName")
    private String name;

    private String imageUrl;
    private List<GetMenuItemResponse> menuItems;

    public GetCategoriesInShopMenu() {
    }

    public GetCategoriesInShopMenu(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<GetMenuItemResponse> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<GetMenuItemResponse> menuItems) {
        this.menuItems = menuItems;
    }
}
