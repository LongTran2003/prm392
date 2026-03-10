package prm392.orderfood.domain.models.category;

public class CategoriesInShopMenu {
    private String id;
    private String name;

    public CategoriesInShopMenu() {
    }

    public CategoriesInShopMenu(String id, String name) {
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
