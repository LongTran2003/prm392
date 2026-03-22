package prm392.orderfood.androidapp.viewModel;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import prm392.orderfood.androidapp.utils.SingleLiveEvent;
import prm392.orderfood.domain.models.menuItem.MenuItem;
import prm392.orderfood.domain.models.menuItem.MenuItemResponse;
import prm392.orderfood.domain.models.shops.PopularShopResponse;
import prm392.orderfood.domain.models.shops.Shop;
import prm392.orderfood.domain.models.shops.ShopDetailResponse;
import prm392.orderfood.domain.usecase.MenuItemUseCase;
import prm392.orderfood.domain.usecase.ShopUseCase;

@HiltViewModel
public class ShopViewModel extends ViewModel {

    private final ShopUseCase shopUseCase;
    private final MenuItemUseCase mMenuItemUseCase;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<List<Shop>> _shops = new MutableLiveData<>();
    public LiveData<List<Shop>> shops = _shops;

    private final MutableLiveData<List<PopularShopResponse>> _popularShops = new MutableLiveData<>();
    public LiveData<List<PopularShopResponse>> getPopularShopResponse() {
        return _popularShops;
    }

    private final MutableLiveData<Shop> _shopDetail = new MutableLiveData<>();
    public LiveData<Shop> shopDetail = _shopDetail;

    private final MutableLiveData<Boolean> _loading = new MutableLiveData<>(false);
    public LiveData<Boolean> loading = _loading;

    private final MutableLiveData<Boolean> _actionSuccess = new MutableLiveData<>();
    public LiveData<Boolean> actionSuccess = _actionSuccess;

    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public LiveData<String> errorMessage = _errorMessage;

    private final MutableLiveData<Shop> selectedShop = new MutableLiveData<>();
    public LiveData<Shop> getSelectedShop() {
        return selectedShop;
    }
    public void setSelectedShop(Shop shop) {
        selectedShop.setValue(shop);
    }

    private final MutableLiveData<ShopDetailResponse> _shopDetailResponse = new MutableLiveData<>();
    public LiveData<ShopDetailResponse> getShopDetailResponse() {
        return _shopDetailResponse;
    }

    private final MutableLiveData<MenuItemResponse> selectedMenuItem = new MutableLiveData<>();
    public LiveData<MenuItemResponse> getSelectedMenuItem() {
        return selectedMenuItem;
    }
    public void setSelectedMenuItem(MenuItemResponse menuItem) {
        selectedMenuItem.setValue(menuItem);
    }

    private final SingleLiveEvent<String> toastMessage = new SingleLiveEvent<>();
    public LiveData<String> getToastMessage() {
        return toastMessage;
    }


    @Inject
    public ShopViewModel(ShopUseCase shopUseCase, MenuItemUseCase menuItemUseCase) {
        this.shopUseCase = shopUseCase;
        this.mMenuItemUseCase = menuItemUseCase;
    }

    public void loadShopsByStatus(String status, int pageIndex, int pageSize) {
        _loading.setValue(true);
        disposables.add(
                shopUseCase.getShopsByStatus(status, pageIndex, pageSize)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                shops -> {
                                    _shops.setValue(shops);
                                    _loading.setValue(false);
                                },
                                error -> handleError("Load shops by status", error)
                        )
        );
    }

    public void fetchPopularShops(String curTime) {
        _loading.setValue(true);
        disposables.add(
                shopUseCase.getPopularShops(curTime)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                _popularShops::setValue,
                                error -> handleError("Fetch popular shops", error)
                        )
        );
    }

    public void loadShopsByOwner(int pageIndex, int pageSize) {
        _loading.setValue(true);
        disposables.add(
                shopUseCase.getShopsByOwner(pageIndex, pageSize)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                shops -> {
                                    _shops.setValue(shops);
                                    _loading.setValue(false);
                                },
                                error -> handleError("Load shops by owner", error)
                        )
        );
    }

    public void getShopById(String shopId) {
        _loading.setValue(true);
        disposables.add(
                shopUseCase.getShopById(shopId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                shop -> {
                                    _shopDetail.setValue(shop);
                                    _loading.setValue(false);
                                },
                                error -> handleError("Get shop by ID", error)
                        )
        );
    }

    public void createShop(Shop shop, File image, File businessImage, List<File> subImages) {
        _loading.setValue(true);
        disposables.add(
                shopUseCase.createShop(shop, image, businessImage, subImages)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                createdShop -> {
                                    _shopDetail.setValue(createdShop);
                                    _actionSuccess.setValue(true);
                                    _loading.setValue(false);
                                },
                                error -> handleError("Create shop", error)
                        )
        );
    }

    public void updateShop(Shop shop, File image, File businessImage, List<File> subImages) {
        _loading.setValue(true);
        disposables.add(
                shopUseCase.updateShop(shop, image, businessImage, subImages)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                updatedShop -> {
                                    _shopDetail.setValue(updatedShop);
                                    _actionSuccess.setValue(true);
                                    _loading.setValue(false);
                                },
                                error -> handleError("Update shop", error)
                        )
        );
    }

    public void deleteShop(String shopId) {
        _loading.setValue(true);
        disposables.add(
                shopUseCase.deleteShop(shopId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                success -> {
                                    _actionSuccess.setValue(success);
                                    _loading.setValue(false);
                                },
                                error -> handleError("Delete shop", error)
                        )
        );
    }

    public void getShopDetail(String shopId) {
        _loading.setValue(true);
        disposables.add(
                shopUseCase.getShopDetail(shopId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                shopDetailResponse -> {
                                    _shopDetailResponse.setValue(shopDetailResponse);
                                    _loading.setValue(false);
                                },
                                error -> handleError("Get shop detail", error)
                        )
        );
    }

    public void approveOrRejectShop(String shopId, boolean isApproved) {
        _loading.setValue(true);
        disposables.add(
                shopUseCase.approveOrRejectShop(shopId, isApproved)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                success -> {
                                    _actionSuccess.setValue(success);
                                    _loading.setValue(false);
                                },
                                error -> handleError("Approve/Reject shop", error)
                        )
        );
    }

    public void addItemToShop(MenuItem menuItem, File imgFile) {
        _loading.setValue(true);
        disposables.add(
                mMenuItemUseCase.createMenuItem(menuItem, imgFile)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                success -> {
                                    _loading.setValue(false);
                                    ShopDetailResponse current = _shopDetailResponse.getValue();
                                    if (current != null && success.body() != null) {
                                        current.getMenuItems().add(success.body());
                                        _shopDetailResponse.setValue(current);
                                    }
                                    toastMessage.setValue("Menu item added successfully");
                                },
                                error -> handleError("Add item to shop", error)
                        )
        );
    }

    public void updateItemToShop(String id, MenuItem menuItem, File imgFile) {
        _loading.setValue(true);
        disposables.add(
                mMenuItemUseCase.updateMenuItem(id, menuItem, imgFile)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    _loading.setValue(false);
                                    MenuItemResponse updatedItem = response;
                                    ShopDetailResponse current = Objects.requireNonNull(_shopDetailResponse.getValue());
                                    current.getMenuItems().removeIf(item -> item.getId().equals(updatedItem.getId()));
                                    current.getMenuItems().add(updatedItem);
                                    _shopDetailResponse.setValue(current);
                                    toastMessage.setValue("Menu item updated successfully");
                                },
                                error -> handleError("Update item to shop", error)
                        )
        );
    }

    public void deleteMenuItem(String menuItemId) {
        disposables.add(
                mMenuItemUseCase.deleteMenuItem(menuItemId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    ShopDetailResponse current = Objects.requireNonNull(_shopDetailResponse.getValue());
                                    current.getMenuItems().removeIf(item -> item.getId().equals(menuItemId));
                                    //Phải setValue lại để cập nhật UI
                                    _shopDetailResponse.setValue(current);

                                    toastMessage.setValue("Menu item deleted successfully");
                                },
                                error -> handleError("Delete menu item", error)
                        )
        );
    }

    private void handleError(String source, Throwable error) {
        Log.e("ShopViewModel", source + " failed", error);
        _errorMessage.setValue(source + " failed: " + error.getMessage());
        _loading.setValue(false);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
