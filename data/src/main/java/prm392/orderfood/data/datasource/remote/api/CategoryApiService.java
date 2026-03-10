package prm392.orderfood.data.datasource.remote.api;

import java.util.List;

import io.reactivex.Single;
import prm392.orderfood.data.datasource.remote.modelResponse.ApiResponse;
import prm392.orderfood.data.datasource.remote.modelResponse.shop.GetShopDetailResponse;
import prm392.orderfood.domain.models.category.CategoryResponse;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CategoryApiService {
    @GET("api/v1/categories")
    Single<ApiResponse<List<CategoryResponse>>> getAll();
}
