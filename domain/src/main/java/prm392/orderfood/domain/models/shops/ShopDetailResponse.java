package prm392.orderfood.domain.models.shops;

import java.util.ArrayList;
import java.util.List;

import prm392.orderfood.domain.models.category.CategoriesInShopMenu;
import prm392.orderfood.domain.models.menuItem.MenuItemResponse;

public class ShopDetailResponse {
    private String id;
    private String name;
    private String address;
    private String imageUrl;
    private String openHours;
    private String endHours;
    private double rating;
    private String status;
    private String businessImageUrl;
    private String note;
    private double latitude;
    private double longitude;
    private List<String> images;
    private List<CategoriesInShopMenu> categories;
    private List<MenuItemResponse> menuItems;

    public ShopDetailResponse() {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }

    public String getEndHours() {
        return endHours;
    }

    public void setEndHours(String endHours) {
        this.endHours = endHours;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<CategoriesInShopMenu> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoriesInShopMenu> categories) {
        this.categories = categories;
    }

    public List<MenuItemResponse> getMenuItems() {
        if (menuItems == null) {
            menuItems = new ArrayList<>();
        }
        return menuItems;
    }

    public void setMenuItems(List<MenuItemResponse> menuItems) {
        this.menuItems = menuItems;
    }

    public String getBusinessImageUrl() {
        return businessImageUrl;
    }

    public void setBusinessImageUrl(String businessImageUrl) {
        this.businessImageUrl = businessImageUrl;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
