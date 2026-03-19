package prm392.orderfood.data.datasource.remote.api;

import java.util.List;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import prm392.orderfood.data.datasource.remote.modelRequest.shop.ApproveShopRequest;
import prm392.orderfood.data.datasource.remote.modelResponse.ApiResponse;
import prm392.orderfood.data.datasource.remote.modelResponse.PagingResponse;
import prm392.orderfood.data.datasource.remote.modelResponse.shop.GetShopDetailResponse;
import prm392.orderfood.data.datasource.remote.modelResponse.shop.GetShopResponse;
import prm392.orderfood.domain.models.shops.PopularShopResponse;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ShopApiService {
    @Multipart
    @POST("api/shops")
    Single<Response<ApiResponse<GetShopResponse>>> createShop(
            @Part("ShopName") RequestBody shopName,
            @Part("Description") RequestBody description,
            @Part("Address") RequestBody address,
            @Part("OpenHours") RequestBody openHours,
            @Part("CloseHours") RequestBody closeHours,
            @Part("Latitude") RequestBody latitude,
            @Part("Longitude") RequestBody longitude,
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part businessLicenseImage
    );

    @Multipart
    @PUT("api/shops")
    Single<Response<ApiResponse<GetShopResponse>>> updateShop(
            @Part("ShopId") RequestBody shopId,
            @Part("ShopName") RequestBody shopName,
            @Part("Description") RequestBody description,
            @Part("Address") RequestBody address,
            @Part("OpenHours") RequestBody openHours,
            @Part("CloseHours") RequestBody closeHours,
            @Part("Latitude") RequestBody latitude,
            @Part("Longitude") RequestBody longitude,
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part businessLicenseImage
    );

    @DELETE("api/shops")
    Single<Response<ApiResponse<String>>> deleteShop(
            @Query("shopId") String shopId
    );

    @GET("api/shops/shop-owner")
    Single<Response<ApiResponse<PagingResponse<GetShopResponse>>>> getShopsByOwner(
            @Query("PageIndex") int pageIndex,
            @Query("PageSize") int pageSize
    );

    @GET("api/shops/detail/{shopId}")
    Single<Response<ApiResponse<GetShopDetailResponse>>> getShopDetail(
            @Path("shopId") String shopId
    );

    @GET("api/shops/status/{status}")
    Single<Response<ApiResponse<PagingResponse<GetShopResponse>>>> getShopsByStatus(
            @Path("status") String status,
            @Query("PageIndex") int pageIndex,
            @Query("PageSize") int pageSize
    );

    @POST("api/shops/approve-reject")
    Single<Response<ApiResponse<String>>> approveOrRejectShop(
            @Body ApproveShopRequest request
    );

    @GET("api/shops/popular")
    Single<ApiResponse<List<PopularShopResponse>>> getPopularShops();
}