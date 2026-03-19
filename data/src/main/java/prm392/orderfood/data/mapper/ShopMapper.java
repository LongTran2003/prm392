package prm392.orderfood.data.mapper;

import java.util.ArrayList;
import java.util.List;

import prm392.orderfood.data.datasource.remote.modelResponse.category.GetCategoriesInShopMenu;
import prm392.orderfood.data.datasource.remote.modelResponse.menuItem.GetMenuItemResponse;
import prm392.orderfood.data.datasource.remote.modelResponse.shop.GetShopDetailResponse;
import prm392.orderfood.data.datasource.remote.modelResponse.shop.GetShopImageResponse;
import prm392.orderfood.data.datasource.remote.modelResponse.shop.GetShopResponse;
import prm392.orderfood.domain.models.shops.Shop;
import prm392.orderfood.domain.models.shops.ShopDetailResponse;

public class ShopMapper {

    public static Shop toDomain(GetShopResponse response) {
        if (response == null) return null;

        Shop shop = new Shop();
        shop.setId(response.getId());
        shop.setName(response.getName());
        shop.setAddress(response.getAddress());
        shop.setImageUrl(response.getImageUrl());
        shop.setOpenHours(response.getOpenHours());
        shop.setEndHours(response.getEndHours());
        shop.setRating(response.getRating());
        shop.setStatus(response.getStatus());
        shop.setLatitude(response.getLatitude());
        shop.setLongitude(response.getLongitude());
        shop.setBusinessImageUrl(response.getBusinessImageUrl());
        shop.setOwnerId(response.getOwnerId());

        if (response.getOwner() != null) {
            shop.setOwner(UserMapper.mapToUserProfileDomain(response.getOwner()));
        }

        List<String> subImages = new ArrayList<>();
        if (response.getImages() != null) {
            for (GetShopImageResponse img : response.getImages()) {
                subImages.add(img.getImageUrl());
            }
        }
        shop.setImages(subImages);

        return shop;
    }

    public static List<Shop> toDomainList(List<GetShopResponse> responseList) {
        List<Shop> shops = new ArrayList<>();
        if (responseList != null && !responseList.isEmpty()) {
            for (GetShopResponse response : responseList) {
                Shop shop = toDomain(response);
                if (shop != null) {
                    shops.add(shop);
                }
            }
        }
        return shops;
    }

    public static ShopDetailResponse toDomain(GetShopDetailResponse response) {
        if (response == null) return null;

        ShopDetailResponse shop = new ShopDetailResponse();
        shop.setId(response.getId());
        shop.setName(response.getName());
        shop.setAddress(response.getAddress());
        shop.setImageUrl(response.getImageUrl());
        shop.setOpenHours(response.getOpenHours());
        shop.setEndHours(response.getEndHours());
        shop.setRating(response.getRating());
        shop.setStatus(response.getStatus());

        // SubImages
        List<String> subImages = new ArrayList<>();
        if (response.getImages() != null) {
            for (GetShopImageResponse img : response.getImages()) {
                subImages.add(img.getImageUrl());
            }
        }
        shop.setImages(subImages);

        // Categories
        if (response.getCategories() != null) {
            shop.setCategories(CategoryMapper.mapToDomainList(response.getCategories()));
        }

        // MenuItems
        if (response.getMenuItems() != null) {
            shop.setMenuItems(MenuItemMapper.mapToDomainList(response.getMenuItems()));
        }
        return shop;
    }
}
