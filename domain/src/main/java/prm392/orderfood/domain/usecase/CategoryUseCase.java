package prm392.orderfood.domain.usecase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import prm392.orderfood.domain.models.category.CategoryResponse;
import prm392.orderfood.domain.repositories.CategoryRepository;
import prm392.orderfood.domain.repositories.ShopRepository;
import retrofit2.Response;

public class CategoryUseCase {
    private final CategoryRepository categoryRepository;

    @Inject
    public CategoryUseCase(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Single<Response<List<CategoryResponse>>> getAllCategories() {
        return categoryRepository.getAllCategories()
                .subscribeOn(Schedulers.io());
    }
}
