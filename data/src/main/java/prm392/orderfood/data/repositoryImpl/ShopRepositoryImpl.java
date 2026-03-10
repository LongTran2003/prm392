package prm392.orderfood.data.repositoryImpl;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import prm392.orderfood.data.datasource.remote.ShopDataSource;
import prm392.orderfood.data.datasource.remote.modelRequest.shop.ApproveShopRequest;
import prm392.orderfood.data.datasource.remote.modelResponse.ApiResponse;
import prm392.orderfood.data.datasource.remote.modelResponse.PagingResponse;
import prm392.orderfood.data.datasource.remote.modelResponse.shop.GetShopDetailResponse;
import prm392.orderfood.data.datasource.remote.modelResponse.shop.GetShopResponse;
import prm392.orderfood.data.mapper.ShopMapper;
import prm392.orderfood.domain.models.shops.PopularShopResponse;
import prm392.orderfood.domain.models.shops.Shop;
import prm392.orderfood.domain.models.shops.ShopDetailResponse;
import prm392.orderfood.domain.repositories.ShopRepository;
import retrofit2.Response;

public class ShopRepositoryImpl implements ShopRepository {
    private final ShopDataSource dataSource;

    @Inject
    public ShopRepositoryImpl(ShopDataSource dataSource) {
        this.dataSource = dataSource;
    }

    // ShopOwner
    @Override
    public Single<Shop> createShop(Shop shop, File image, File businessImage, List<File> subImages) {
        return dataSource.createShop(
                        shop.getName(),
                        shop.getAddress(),
                        shop.getOpenHours(),
                        shop.getEndHours(),
                        String.valueOf(shop.getLatitude()),
                        String.valueOf(shop.getLongitude()),
                        image,
                        businessImage,
                        subImages
                )
                .map(Response::body)
                .map(ApiResponse::getData)
                .map(ShopMapper::toDomain) // mapping to domain
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Shop> updateShop(Shop shop, File image, File businessImage, List<File> subImages) {
        return dataSource.updateShop(
                        shop.getId(),
                        shop.getName(),
                        shop.getAddress(),
                        shop.getOpenHours(),
                        shop.getEndHours(),
                        String.valueOf(shop.getLatitude()),
                        String.valueOf(shop.getLongitude()),
                        image,
                        businessImage,
                        subImages
                )
                .map(Response::body)
                .map(ApiResponse::getData)
                .map(ShopMapper::toDomain)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<List<Shop>> getShopsByOwner(int pageIndex, int pageSize) {
        return dataSource.getShopsByOwner(pageIndex, pageSize)
                .map(Response::body)
                .map(apiResponse -> {
                    if (apiResponse == null) {
                        Log.e("ShopRepo", "apiResponse is null");
                        return new ArrayList<GetShopResponse>();
                    }

                    if (!apiResponse.isSuccess()) {
                        Log.e("ShopRepo", "API returned unsuccessful: " + apiResponse.getMessage());
                        return new ArrayList<GetShopResponse>();
                    }

                    if (apiResponse.getData() == null) {
                        Log.e("ShopRepo", "apiResponse.getData() is null");
                        return new ArrayList<GetShopResponse>();
                    }

                    if (apiResponse.getData().getItems() == null) {
                        Log.e("ShopRepo", "getItems() is null");
                        return new ArrayList<GetShopResponse>();
                    }

                    return apiResponse.getData().getItems();
                })
                .map(ShopMapper::toDomainList)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Shop> getShopById(String shopId) {
        return dataSource.getShopById(shopId)
                .map(Response::body)
                .flatMap(apiResponse -> {
                    if (apiResponse == null || !apiResponse.isSuccess() || apiResponse.getData() == null) {
                        return Single.error(new IllegalStateException("Không tìm thấy shop hoặc API thất bại"));
                    }
                    return Single.just(ShopMapper.toDomain(apiResponse.getData()));
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Boolean> deleteShop(String shopId) {
        return dataSource.deleteShop(shopId)
                .map(Response::isSuccessful)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<ShopDetailResponse> getShopDetail(String shopId) {
        return dataSource.getShopDetail(shopId)
                .map(Response::body)
                .flatMap(apiResponse -> {
                    if (apiResponse == null || !apiResponse.isSuccess() || apiResponse.getData() == null) {
                        return Single.error(new IllegalStateException("Shop detail not found or API failed"));
                    }
                    GetShopDetailResponse shopDetail = apiResponse.getData();
                    ShopDetailResponse shop = ShopMapper.toDomain(shopDetail);
                    return Single.just(shop);
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<List<PopularShopResponse>> getPopularShops(String curTime) {
        return dataSource.getPopularShops(curTime)
                .subscribeOn(Schedulers.io())
                .map(apiResponse -> {
                    if (apiResponse == null || apiResponse.getData() == null) {
                        Log.e("ShopRepo", "Popular shops API response is null or data is null");
                        return new ArrayList<PopularShopResponse>();
                    }
                    return apiResponse.getData();
                });

    }

    // Admin
    @Override
    public Single<List<Shop>> getShopsByStatus(String status, int pageIndex, int pageSize) {
        return dataSource.getShopsByStatus(status, pageIndex, pageSize)
                .map(Response::body)
                .map(ApiResponse::getData)
                .map(PagingResponse::getItems)
                .map(ShopMapper::toDomainList)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Boolean> approveOrRejectShop(String shopId, boolean isApproved) {
        ApproveShopRequest request = new ApproveShopRequest(shopId, isApproved);
        return dataSource.approveOrRejectShop(request)
                .map(Response::isSuccessful)
                .subscribeOn(Schedulers.io());
    }


}
