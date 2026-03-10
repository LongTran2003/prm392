package prm392.orderfood.domain.repositories;

import androidx.annotation.Nullable;

import java.io.File;
import java.util.List;

import io.reactivex.Single;
import prm392.orderfood.domain.models.shops.PopularShopResponse;
import prm392.orderfood.domain.models.shops.Shop;
import prm392.orderfood.domain.models.shops.ShopDetailResponse;

public interface ShopRepository {

    // Admin
    Single<List<Shop>> getShopsByStatus(String status, int pageIndex, int pageSize);
    Single<Boolean> approveOrRejectShop(String shopId, boolean isApproved);

    // ShopOwner
    Single<Shop> createShop(Shop shop, File image, File businessImage, List<File> subImages);
    Single<Shop> updateShop(Shop shop, @Nullable File image, @Nullable File businessImage, @Nullable List<File> subImages);
    Single<List<Shop>> getShopsByOwner(int pageIndex, int pageSize);
    Single<Shop> getShopById(String shopId);
    Single<Boolean> deleteShop(String shopId); // ShopOwner

    Single<ShopDetailResponse> getShopDetail(String shopId);
    Single<List<PopularShopResponse>> getPopularShops(String curTime);
}
