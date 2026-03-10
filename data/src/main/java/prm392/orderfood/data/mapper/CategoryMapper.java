package prm392.orderfood.data.mapper;

import java.util.ArrayList;
import java.util.List;

import prm392.orderfood.data.datasource.remote.modelResponse.category.GetCategoriesInShopMenu;
import prm392.orderfood.domain.models.category.CategoriesInShopMenu;

public class CategoryMapper {
    public static CategoriesInShopMenu mapToDomain(GetCategoriesInShopMenu category) {
        if (category == null) return null;

        CategoriesInShopMenu categories = new CategoriesInShopMenu();
        categories.setId(category.getId());
        categories.setName(category.getName());

        return categories;
    }

    public static List<CategoriesInShopMenu> mapToDomainList(List<GetCategoriesInShopMenu> categories) {
        if (categories == null || categories.isEmpty()) return null;

        var list = new ArrayList<CategoriesInShopMenu>();

        for (GetCategoriesInShopMenu category : categories) {
            CategoriesInShopMenu mappedCategory = mapToDomain(category);
            if (mappedCategory != null) {
                list.add(mappedCategory);
            }
        }
        return list;
    }
}
