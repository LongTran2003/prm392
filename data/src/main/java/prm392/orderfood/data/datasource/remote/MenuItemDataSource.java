package prm392.orderfood.data.datasource.remote;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import prm392.orderfood.data.datasource.remote.api.MenuItemApiService;
import prm392.orderfood.data.datasource.remote.modelResponse.ApiResponse;
import prm392.orderfood.domain.models.menuItem.MenuItem;
import prm392.orderfood.domain.models.menuItem.MenuItemResponse;

public class MenuItemDataSource {
    private final MenuItemApiService api;

    @Inject
    public MenuItemDataSource(MenuItemApiService api) {
        this.api = api;
    }

    public Single<ApiResponse<MenuItemResponse>> createMenuItem(MenuItem menuItem, File img) {
        return api.createMenuItem(
            toPart(menuItem.getName()),              // MenuItemName
            toPart(menuItem.getDescription()),       // Description
            toPart(String.valueOf(menuItem.getPrice())),  // Price
            toPart(menuItem.getCategoryId()),        // CategoryId
            toPart(menuItem.getShopId()),            // ShopId
            toPart(menuItem.isAvailable() ? "true" : "false"),  // IsAvailable
            toMultipartBody("image", img)            // image
        );
    }

    public Single<ApiResponse<MenuItemResponse>> updateMenuItem(String id, MenuItem menuItem, File img) {
        return api.updateMenuItem(
            id,
            toPart(menuItem.getName()),              // MenuItemName
            toPart(menuItem.getDescription()),       // Description
            toPart(String.valueOf(menuItem.getPrice())),  // Price
            toPart(menuItem.getCategoryId()),        // CategoryId
            toPart(menuItem.getShopId()),            // ShopId
            toPart(menuItem.isAvailable() ? "true" : "false"),  // IsAvailable
            img != null ? toMultipartBody("image", img) : null  // image
        );
    }

    public Single<ApiResponse<String>> deleteMenuItem(String menuItemId) {
        return api.deleteMenuItem(menuItemId);
    }

    public Single<ApiResponse<List<MenuItemResponse>>> getAllMenuItems() {
        return api.getAllMenuItems();
    }

    private MultipartBody.Part toMultipartBody(String name, File file) {
        RequestBody req = RequestBody.create(file, MediaType.parse("image/*"));
        return MultipartBody.Part.createFormData(name, file.getName(), req);
    }

    private RequestBody toPart(String value) {
        return RequestBody.create(value, MediaType.parse("text/plain"));
    }
}
