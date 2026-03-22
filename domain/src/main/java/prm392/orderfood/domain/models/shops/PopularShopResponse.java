package prm392.orderfood.domain.models.shops;

import java.util.List;


public class PopularShopResponse {
    private String shopId;
    private String shopName;
    private String imageUrl;
    private String imgeUrl;
    private String address;
    private String status;
    private String openHours;
    private String closeHours;
    private double averageRating;
    private double latitude;
    private double longitude;
    private List<String> categoryIds;

    public PopularShopResponse(String id, String name, String imageUrl, String address, String status,
                               String openHours, String endHours, double rating, List<String> categoryIds, double latitude, double longitude) {
        this.shopId = id;
        this.shopName = name;
        this.imageUrl = imageUrl;
        this.address = address;
        this.status = status;
        this.openHours = openHours;
        this.closeHours = endHours;
        this.averageRating = rating;
        this.categoryIds = categoryIds;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public PopularShopResponse() {
    }

    public String getId() {
        return shopId;
    }

    public void setId(String id) {
        this.shopId = id;
    }

    public String getName() {
        return shopName;
    }

    public void setName(String name) {
        this.shopName = name;
    }

    public String getImageUrl() {
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            return imageUrl;
        }
        return imgeUrl;
    }
    public String getImgeUrl() {
        return imgeUrl;
    }

    public void setImgeUrl(String imgeUrl) {
        this.imgeUrl = imgeUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }

    public String getEndHours() {
        return closeHours;
    }

    public void setEndHours(String endHours) {
        this.closeHours = endHours;
    }

    public double getRating() {
        return averageRating;
    }

    public void setRating(double rating) {
        this.averageRating = rating;
    }

    public List<String> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<String> categoryIds) {
        this.categoryIds = categoryIds;
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
