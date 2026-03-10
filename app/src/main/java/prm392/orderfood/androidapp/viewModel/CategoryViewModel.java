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

    @Override
    protected void onCleared() {
        super.onCleared();
        mCompositeDisposable.clear(); // Giải phóng bộ nhớ khi ViewModel bị hủy
    }

}
