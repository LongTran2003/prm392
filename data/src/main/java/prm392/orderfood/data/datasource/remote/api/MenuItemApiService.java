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
    @POST("api/v1/MenuItem")
    Single<ApiResponse<MenuItemResponse>> createMenuItem(
            @Part("name") RequestBody name,
            @Part("price") RequestBody price,
            @Part("description") RequestBody description,
            @Part("imageUrl") RequestBody imageUrl,
            @Part("isAvailable") RequestBody isAvailable,
            @Part("shopId") RequestBody shopId,
            @Part("categoryId") RequestBody categoryId,
            @Part MultipartBody.Part image
    );

    @Multipart
    @PUT("api/v1/MenuItem/{id}")
    Single<ApiResponse<MenuItemResponse>> updateMenuItem(
            @Path("id") String id,
            @Part("name") RequestBody name,
            @Part("description") RequestBody description,
            @Part("price") RequestBody price,
            @Part("imageUrl") RequestBody imageUrl,
            @Part("isAvailable") RequestBody isAvailable,
            @Part("categoryId") RequestBody categoryId,
            @Part("shopId") RequestBody shopId,
            @Part MultipartBody.Part image
    );

    @DELETE("api/v1/MenuItem/{id}")
    Single<ApiResponse<String>> deleteMenuItem(@Path("id") String menuItemId);

    @GET("api/v1/MenuItem")
    Single<ApiResponse<List<MenuItemResponse>>> getAllMenuItems();
}
