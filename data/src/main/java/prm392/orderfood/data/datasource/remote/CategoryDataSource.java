package prm392.orderfood.data.datasource.remote;

import java.util.List;

import javax.inject.Inject;

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
}
