package prm392.orderfood.data.datasource.remote.modelResponse.shop;

import java.util.List;

import prm392.orderfood.data.datasource.remote.modelResponse.category.GetCategoriesInShopMenu;
import prm392.orderfood.data.datasource.remote.modelResponse.menuItem.GetMenuItemResponse;
import prm392.orderfood.data.datasource.remote.modelResponse.user.GetUserResponse;

public class GetShopDetailResponse {
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
    private List<GetShopImageResponse> images;
    private List<GetCategoriesInShopMenu> categories;
    private List<GetMenuItemResponse> menuItems;

    public GetShopDetailResponse(String id, String name, String address, String imageUrl, String openHours,
                                 String endHours, double rating, String status, List<GetShopImageResponse> images,
                                 List<GetCategoriesInShopMenu> categories, List<GetMenuItemResponse> menuItems,
                                 String businessImageUrl, String note, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.imageUrl = imageUrl;
        this.openHours = openHours;
        this.endHours = endHours;
        this.rating = rating;
        this.status = status;
        this.images = images;
        this.categories = categories;
        this.menuItems = menuItems;
        this.businessImageUrl = businessImageUrl;
        this.note = note;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public List<GetShopImageResponse> getImages() {
        return images;
    }

    public void setImages(List<GetShopImageResponse> images) {
        this.images = images;
    }

    public List<GetCategoriesInShopMenu> getCategories() {
        return categories;
    }

    public void setCategories(List<GetCategoriesInShopMenu> categories) {
        this.categories = categories;
    }

    public List<GetMenuItemResponse> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<GetMenuItemResponse> menuItems) {
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
