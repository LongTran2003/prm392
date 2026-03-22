package prm392.orderfood.androidapp.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import prm392.orderfood.androidapp.utils.SingleLiveEvent;
import prm392.orderfood.domain.models.category.CategoryResponse;
import prm392.orderfood.domain.usecase.CategoryUseCase;

@HiltViewModel
public class CategoryViewModel extends ViewModel {
    private CategoryUseCase categoryUseCase;
    private final CompositeDisposable mCompositeDisposable;

    @Inject
    public CategoryViewModel(CategoryUseCase categoryUseCase) {
        this.categoryUseCase = categoryUseCase;
        mCompositeDisposable = new CompositeDisposable();
    }

    private MutableLiveData<List<CategoryResponse>> categoriesLiveData = new MutableLiveData<>();

    public LiveData<List<CategoryResponse>> getCategoriesLiveData() {
        return categoriesLiveData;
    }

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<List<CategoryResponse>> allCategories = new MutableLiveData<>();
    public LiveData<List<CategoryResponse>> getAllCategoriesLiveData() {
        return allCategories;
    }

    private final SingleLiveEvent<String> errorMessage = new SingleLiveEvent<>();
    private LiveData<String> getErrorMessageLiveData() {
        return errorMessage;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    private final SingleLiveEvent<String> toastMessage = new SingleLiveEvent<>();
    public LiveData<String> getToastMessage() {
        return toastMessage;
    }

    private final SingleLiveEvent<Boolean> createSuccess = new SingleLiveEvent<>();
    public LiveData<Boolean> getCreateSuccess() {
        return createSuccess;
    }
    private final SingleLiveEvent<Boolean> deleteSuccess = new SingleLiveEvent<>();
    public LiveData<Boolean> getDeleteSuccess() {
        return deleteSuccess;
    }


    public void getAllCategories() {
        mCompositeDisposable.add(
                categoryUseCase.getAllCategories()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response.isSuccessful()) {
                                        categoriesLiveData.setValue(response.body());
                                    } else {
                                        errorMessage.setValue("Error: " + response.code() + " - " + response.message());
                                    }
                                },
                                throwable -> {
                                    errorMessage.setValue("Error: " + throwable.getMessage());
                                }
                        )
        );
    }

    public void createCategory(String name, String description, String imageUrl) {
        isLoading.setValue(true);

        mCompositeDisposable.add(
                categoryUseCase.createCategory(name, description, imageUrl)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    isLoading.setValue(false);

                                    if (response.isSuccessful() && response.body() != null) {
                                        toastMessage.setValue("Create category successfully");
                                        createSuccess.setValue(true);

                                    // reload list để spinner Add Product có dữ liệu mới
                                        getAllCategories();
                                    } else {
                                        errorMessage.setValue("Create category failed: " + response.code() + " - " + response.message());
                                    }
                                },
                                throwable -> {
                                    isLoading.setValue(false);
                                    errorMessage.setValue("Create category failed: " + throwable.getMessage());
                                }
                        )
        );
    }

    public void deleteCategory(String categoryId) {
        if (categoryId == null || categoryId.isEmpty()) {
            errorMessage.setValue("Invalid category id");
            return;
        }

        isLoading.setValue(true);
        mCompositeDisposable.add(
                categoryUseCase.deleteCategory(categoryId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    isLoading.setValue(false);
                                    if (response.isSuccessful() && Boolean.TRUE.equals(response.body())) {
                                        toastMessage.setValue("Delete category successfully");
                                        deleteSuccess.setValue(true);
                                        getAllCategories();
                                    } else {
                                        errorMessage.setValue("Delete category failed: " + response.code() + " - " + response.message());
                                    }
                                },
                                throwable -> {
                                    isLoading.setValue(false);
                                    errorMessage.setValue("Delete category failed: " + throwable.getMessage());
                                }
                        )
        );

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mCompositeDisposable.clear(); // Giải phóng bộ nhớ khi ViewModel bị hủy
    }

}
