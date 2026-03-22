package prm392.orderfood.data.repositoryImpl;

import java.util.List;

import javax.inject.Inject;
import okhttp3.ResponseBody;
import prm392.orderfood.data.datasource.remote.modelRequest.category.CreateCategoryRequest;
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

    @Override
    public Single<Response<CategoryResponse>> createCategory(String name, String description, String imageUrl) {
        CreateCategoryRequest request = new CreateCategoryRequest(name, description, imageUrl);
        return categoryDataSource.createCategory(request)
                .map(apiResponse -> {
                    if (apiResponse != null && apiResponse.isSuccess()) {
                        return Response.success(apiResponse.getData());
                    }
                    String msg = apiResponse != null && apiResponse.getMessage() != null
                            ? apiResponse.getMessage()
                            : "Create category failed";
                    return Response.<CategoryResponse>error(400, ResponseBody.create(msg, null));
                })
                .onErrorReturn((Throwable throwable) -> Response.<CategoryResponse>error(
                        500,
                        ResponseBody.create(
                                throwable.getMessage() != null ? throwable.getMessage() : "Create category failed",
                                null
                        )
                ));
    }

    @Override
    public Single<Response<Boolean>> deleteCategory(String categoryId) {
        return categoryDataSource.deleteCategory(categoryId)
                .map(apiResponse -> {
                    if (apiResponse != null && apiResponse.isSuccess()) {
                        return Response.success(true);
                    }
                    String msg = apiResponse != null && apiResponse.getMessage() != null
                            ? apiResponse.getMessage()
                            : "Delete category failed";
                    return Response.<Boolean>error(400, ResponseBody.create(msg, null));
                })
                .onErrorReturn(throwable -> Response.<Boolean>error(
                        500,
                        ResponseBody.create(
                                throwable.getMessage() != null ? throwable.getMessage() : "Delete category failed",
                                null
                        )
                ));
    }
}
