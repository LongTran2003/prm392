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
                .flatMap(response -> {
                    if (response == null) {
                        return Single.error(new IllegalStateException("Owner shops response is null"));
                    }

                    if (!response.isSuccessful()) {
                        return Single.error(new IllegalStateException(
                                "Owner shops request failed: HTTP " + response.code()));
                    }

                    ApiResponse<PagingResponse<GetShopResponse>> apiResponse = response.body();
                    if (apiResponse == null) {
                        return Single.error(new IllegalStateException("Owner shops body is null"));
                    }

                    if (!apiResponse.isSuccess()) {
                        String message = apiResponse.getMessage() != null
                                ? apiResponse.getMessage()
                                : "Owner shops API failed";
                        return Single.error(new IllegalStateException(message));
                    }

                    PagingResponse<GetShopResponse> paging = apiResponse.getData();
                    if (paging == null || paging.getItems() == null) {
                        return Single.just(new ArrayList<Shop>());
                    }

                    return Single.just(ShopMapper.toDomainList(paging.getItems()));
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Shop> getShopById(String shopId) {  // Return type: Single<Shop>
        return dataSource.getShopDetail(shopId)
                .map(Response::body)
                .flatMap(apiResponse -> {
                    if (apiResponse == null || !apiResponse.isSuccess() || apiResponse.getData() == null) {
                        return Single.error(new IllegalStateException("Không tìm thấy shop hoặc API thất bại"));
                    }
                    GetShopDetailResponse detail = apiResponse.getData();
                    // Map GetShopDetailResponse to Shop manually
                    Shop shop = new Shop();
                    shop.setId(detail.getId());
                    shop.setName(detail.getName());
                    shop.setAddress(detail.getAddress());
                    shop.setImageUrl(detail.getImageUrl());
                    shop.setOpenHours(detail.getOpenHours());
                    shop.setEndHours(detail.getEndHours());
                    shop.setRating(detail.getRating());
                    shop.setStatus(detail.getStatus());
                    shop.setLatitude(detail.getLatitude());
                    shop.setLongitude(detail.getLongitude());
                    shop.setBusinessImageUrl(detail.getBusinessImageUrl());
                    // Add other fields as needed
                    return Single.just(shop);
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
                .flatMap(response -> {
                    if (response == null) {
                        return Single.error(new IllegalStateException("Status shops response is null"));
                    }

                    if (!response.isSuccessful()) {
                        return Single.error(new IllegalStateException(
                                "Status shops request failed: HTTP " + response.code()));
                    }

                    ApiResponse<PagingResponse<GetShopResponse>> apiResponse = response.body();
                    if (apiResponse == null) {
                        return Single.error(new IllegalStateException("Status shops body is null"));
                    }

                    if (!apiResponse.isSuccess()) {
                        String message = apiResponse.getMessage() != null
                                ? apiResponse.getMessage()
                                : "Status shops API failed";
                        return Single.error(new IllegalStateException(message));
                    }

                    PagingResponse<GetShopResponse> paging = apiResponse.getData();
                    if (paging == null || paging.getItems() == null) {
                        return Single.just(new ArrayList<Shop>());
                    }

                    return Single.just(ShopMapper.toDomainList(paging.getItems()));
                })
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
