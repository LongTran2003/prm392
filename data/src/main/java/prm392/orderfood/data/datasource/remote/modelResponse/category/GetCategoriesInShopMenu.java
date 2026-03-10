package prm392.orderfood.data.datasource.remote.modelResponse.category;

public class GetCategoriesInShopMenu {
    private String id;
    private String name;

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
}
