package prm392.orderfood.data.datasource.remote;

import java.util.List;

import javax.inject.Inject;
import prm392.orderfood.data.datasource.remote.modelRequest.category.CreateCategoryRequest;
import io.reactivex.Single;
import prm392.orderfood.data.datasource.remote.api.CategoryApiService;
import prm392.orderfood.data.datasource.remote.modelResponse.ApiResponse;
import prm392.orderfood.domain.models.category.CategoryResponse;

public class CategoryDataSource {
    private final CategoryApiService api;

    @Inject
    public CategoryDataSource(CategoryApiService api) {
        this.api = api;
    }

    public Single<ApiResponse<List<CategoryResponse>>> getAllCategories() {

        return api.getAll();
    }

    public Single<ApiResponse<CategoryResponse>> createCategory(CreateCategoryRequest request) {
        return api.createCategory(request);
    }

    public Single<ApiResponse<String>> deleteCategory(String categoryId) {
        return api.deleteCategory(categoryId);
    }
}
