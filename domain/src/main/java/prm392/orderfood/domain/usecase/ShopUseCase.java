package prm392.orderfood.domain.usecase;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import prm392.orderfood.domain.models.shops.PopularShopResponse;
import prm392.orderfood.domain.models.shops.Shop;
import prm392.orderfood.domain.models.shops.ShopDetailResponse;
import prm392.orderfood.domain.repositories.ShopRepository;

public class ShopUseCase {

    private final ShopRepository shopRepository;

    @Inject
    public ShopUseCase(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    // Admin
    public Single<List<Shop>> getShopsByStatus(String status, int pageIndex, int pageSize) {
        return shopRepository.getShopsByStatus(status, pageIndex, pageSize)
                .subscribeOn(Schedulers.io());
    }

    public Single<Boolean> approveOrRejectShop(String shopId, boolean isApproved) {
        return shopRepository.approveOrRejectShop(shopId, isApproved)
                .subscribeOn(Schedulers.io());
    }

    // ShopOwner
    public Single<Shop> createShop(Shop shop, File image, File businessImage, List<File> subImages) {
        return shopRepository.createShop(shop, image, businessImage, subImages)
                .subscribeOn(Schedulers.io());
    }

    public Single<Shop> updateShop(Shop shop, File image, File businessImage, List<File> subImages) {
        return shopRepository.updateShop(shop, image, businessImage, subImages)
                .subscribeOn(Schedulers.io());
    }

    public Single<List<Shop>> getShopsByOwner(int pageIndex, int pageSize) {
        return shopRepository.getShopsByOwner(pageIndex, pageSize)
                .subscribeOn(Schedulers.io());
    }

    public Single<Shop> getShopById(String shopId) {
        return shopRepository.getShopById(shopId)
                .subscribeOn(Schedulers.io());
    }

    public Single<Boolean> deleteShop(String shopId) {
        return shopRepository.deleteShop(shopId)
                .subscribeOn(Schedulers.io());
    }

    public Single<ShopDetailResponse> getShopDetail(String shopId) {
        return shopRepository.getShopDetail(shopId)
                .subscribeOn(Schedulers.io());
    }

    public Single<List<PopularShopResponse>> getPopularShops(String curTime) {
        return shopRepository.getPopularShops(curTime)
                .subscribeOn(Schedulers.io());
    }
}
