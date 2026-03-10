package prm392.orderfood.data.repositoryImpl;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import prm392.orderfood.data.datasource.remote.CategoryDataSource;
import prm392.orderfood.domain.models.category.CategoryResponse;
import prm392.orderfood.domain.repositories.CategoryRepository;
import retrofit2.Response;

public class CategoryRepositoryImpl implements CategoryRepository {
    private final CategoryDataSource categoryDataSource;

    @Inject
    public CategoryRepositoryImpl(CategoryDataSource categoryDataSource) {
        this.categoryDataSource = categoryDataSource;
    }

    @Override
    public Single<Response<List<CategoryResponse>>> getAllCategories() {
        return categoryDataSource.getAllCategories()
                .map(apiResponse -> Response.success(apiResponse.getData()))
                .onErrorReturn(throwable -> Response.error(500, null)); // Handle error appropriately
    }
}
