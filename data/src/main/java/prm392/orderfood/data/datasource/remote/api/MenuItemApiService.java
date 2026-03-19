package prm392.orderfood.data.datasource.remote.api;

import java.util.List;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import prm392.orderfood.data.datasource.remote.modelResponse.ApiResponse;
import prm392.orderfood.domain.models.menuItem.MenuItemResponse;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface MenuItemApiService {
    @Multipart
    @POST("api/menu-items")
    Single<ApiResponse<MenuItemResponse>> createMenuItem(
            @Part("MenuItemName") RequestBody menuItemName,
            @Part("Description") RequestBody description,
            @Part("Price") RequestBody price,
            @Part("CategoryId") RequestBody categoryId,
            @Part("ShopId") RequestBody shopId,
            @Part("IsAvailable") RequestBody isAvailable,
            @Part MultipartBody.Part image
    );

    @Multipart
    @PUT("api/menu-items/{menuItemId}")
    Single<ApiResponse<MenuItemResponse>> updateMenuItem(
            @Path("menuItemId") String menuItemId,
            @Part("MenuItemName") RequestBody menuItemName,
            @Part("Description") RequestBody description,
            @Part("Price") RequestBody price,
            @Part("CategoryId") RequestBody categoryId,
            @Part("ShopId") RequestBody shopId,
            @Part("IsAvailable") RequestBody isAvailable,
            @Part MultipartBody.Part image
    );

    @DELETE("api/menu-items/{menuItemId}")
    Single<ApiResponse<String>> deleteMenuItem(@Path("menuItemId") String menuItemId);

    @GET("api/menu-items")
    Single<ApiResponse<List<MenuItemResponse>>> getAllMenuItems();
}