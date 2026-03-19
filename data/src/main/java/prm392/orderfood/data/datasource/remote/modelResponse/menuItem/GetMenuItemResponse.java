package prm392.orderfood.data.datasource.remote.modelResponse.menuItem;

import java.util.Date;
import com.google.gson.annotations.SerializedName;

public class GetMenuItemResponse {
    @SerializedName("menuItemId")
    private String id;

    @SerializedName("menuItemName")
    private String name;

    private double price;
    private String description;
    private String imageUrl;
    private String isAvailable;

    @SerializedName("createdDate")
    private Date createdAt;

    private Date updatedAt;
    private String categoryId;
    private String shopId;

    public GetMenuItemResponse(String id, String name, double price, String description, String imageUrl, String isAvailable, Date createdAt, Date updatedAt, String categoryId, String shopId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.isAvailable = isAvailable;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.categoryId = categoryId;
        this.shopId = shopId;
    }

    public GetMenuItemResponse() {
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}
