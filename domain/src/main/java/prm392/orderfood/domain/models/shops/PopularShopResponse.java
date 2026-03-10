package prm392.orderfood.domain.models.shops;

import java.util.List;

public class PopularShopResponse {
    private String id;
    private String name;
    private String imageUrl;
    private String address;
    private String status;
    private String openHours;
    private String endHours;
    private double rating;
    private double latitude;
    private double longitude;
    private List<String> categoryIds;

    public PopularShopResponse(String id, String name, String imageUrl, String address, String status,
                               String openHours, String endHours, double rating, List<String> categoryIds, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.address = address;
        this.status = status;
        this.openHours = openHours;
        this.endHours = endHours;
        this.rating = rating;
        this.categoryIds = categoryIds;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public PopularShopResponse() {
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
