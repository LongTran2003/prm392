package prm392.orderfood.domain.repositories;

import java.util.List;

import io.reactivex.Single;
import prm392.orderfood.domain.models.category.CategoryResponse;
import retrofit2.Response;

public interface CategoryRepository {
    Single<Response<List<CategoryResponse>>> getAllCategories();
}
