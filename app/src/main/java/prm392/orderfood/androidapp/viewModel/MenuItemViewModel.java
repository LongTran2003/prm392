package prm392.orderfood.androidapp.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import prm392.orderfood.androidapp.utils.SingleLiveEvent;
import prm392.orderfood.domain.models.menuItem.MenuItemResponse;
import prm392.orderfood.domain.usecase.MenuItemUseCase;
import prm392.orderfood.domain.usecase.OrderUseCase;
import prm392.orderfood.domain.usecase.PaymentUseCase;

@HiltViewModel
public class MenuItemViewModel extends ViewModel {
    private final CompositeDisposable mCompositeDisposable;
    private final MenuItemUseCase menuItemUseCase;

    @Inject
    public MenuItemViewModel(MenuItemUseCase menuItemUseCase) {
        this.mCompositeDisposable = new CompositeDisposable();
        this.menuItemUseCase = menuItemUseCase;
    }

    private final SingleLiveEvent<String> toastMessage = new SingleLiveEvent<>();

    public LiveData<String> getToastMessage() {
        return toastMessage;
    }

    private final SingleLiveEvent<String> errorMessage = new SingleLiveEvent<>();

    public LiveData<String> getErrorMessageLiveData() {
        return errorMessage;
    }

    private final MutableLiveData<List<MenuItemResponse>> menuItems = new MutableLiveData<>(new ArrayList<>());
    public LiveData<List<MenuItemResponse>> getMenuItemsLiveData() {
        return menuItems;
    }

    public void getAllMenuItems() {
        mCompositeDisposable.add(
            menuItemUseCase.getAllMenuItems()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response != null && !response.isEmpty()) {
                            menuItems.setValue(response);
                        } else {
                            errorMessage.setValue("No menu items found.");
                        }
                    },
                    throwable -> errorMessage.setValue(throwable.getMessage())
                )
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mCompositeDisposable.clear();
    }

}
