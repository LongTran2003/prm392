package prm392.orderfood.domain.models.category;

import com.google.gson.annotations.SerializedName;

public class CategoryResponse {
    @SerializedName("categoryId")
    private String id;

    @SerializedName("categoryName")
    private String name;
    private String description;

    public CategoryResponse() {
    }

    public CategoryResponse(String id, String name) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
