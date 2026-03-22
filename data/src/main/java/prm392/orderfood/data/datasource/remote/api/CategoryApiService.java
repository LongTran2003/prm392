package prm392.orderfood.data.datasource.remote.api;

import java.util.List;
import prm392.orderfood.data.datasource.remote.modelRequest.category.CreateCategoryRequest;
import retrofit2.http.Body;
import retrofit2.http.POST;
import io.reactivex.Single;
import prm392.orderfood.data.datasource.remote.modelResponse.ApiResponse;
import prm392.orderfood.data.datasource.remote.modelResponse.shop.GetShopDetailResponse;
import prm392.orderfood.domain.models.category.CategoryResponse;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.DELETE;

public interface CategoryApiService {
    @GET("api/categories")
    Single<ApiResponse<List<CategoryResponse>>> getAll();

    @POST("api/categories")
    Single<ApiResponse<CategoryResponse>> createCategory(@Body CreateCategoryRequest request);

    @DELETE("api/categories/{categoryId}")
    Single<ApiResponse<String>> deleteCategory(@Path("categoryId") String categoryId);
}
