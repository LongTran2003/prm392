package prm392.orderfood.data.mapper;

import java.util.ArrayList;
import java.util.List;

import prm392.orderfood.data.datasource.remote.modelResponse.menuItem.GetMenuItemResponse;
import prm392.orderfood.domain.models.menuItem.MenuItemResponse;

public class MenuItemMapper {
    public static MenuItemResponse mapToDomain(GetMenuItemResponse menuItem) {
        if (menuItem == null) return null;

        MenuItemResponse response = new MenuItemResponse();
        response.setId(menuItem.getId());
        response.setName(menuItem.getName());
        response.setDescription(menuItem.getDescription());
        response.setPrice(menuItem.getPrice());
        response.setImageUrl(menuItem.getImageUrl());
        response.setCategoryId(menuItem.getCategoryId());
        response.setShopId(menuItem.getShopId());
        response.setIsAvailable(menuItem.getIsAvailable());
        response.setCreatedAt(menuItem.getCreatedAt());
        response.setUpdatedAt(menuItem.getUpdatedAt());

        return response;
    }

    public static List<MenuItemResponse> mapToDomainList(List<GetMenuItemResponse> menuItems) {
        if (menuItems == null || menuItems.isEmpty()) return null;

        var list = new ArrayList<MenuItemResponse>();

        for (GetMenuItemResponse menuItem : menuItems) {
            MenuItemResponse mappedItem = mapToDomain(menuItem);
            if (mappedItem != null) {
                list.add(mappedItem);
            }
        }
        return list;
    }
}
