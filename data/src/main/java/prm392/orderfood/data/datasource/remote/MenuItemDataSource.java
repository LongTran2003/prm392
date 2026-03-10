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
                toPart(menuItem.getName()),
                toPart(String.valueOf(menuItem.getPrice())),
                toPart(menuItem.getDescription()),
                toPart(menuItem.getImageUrl()),
                toPart(menuItem.isAvailable() ? "true" : "false"),
                toPart(menuItem.getShopId()),
                toPart(menuItem.getCategoryId()),
                toMultipartBody("image", img)
        );
    }

    public Single<ApiResponse<MenuItemResponse>> updateMenuItem(String id, MenuItem menuItem, File img) {
        return api.updateMenuItem(
                id,
                toPart(menuItem.getName()),
                toPart(menuItem.getDescription()),
                toPart(String.valueOf(menuItem.getPrice())),
                toPart(menuItem.getImageUrl()),
                toPart(menuItem.isAvailable() ? "true" : "false"),
                toPart(menuItem.getCategoryId()),
                toPart(menuItem.getShopId()),
                img != null ? toMultipartBody("image", img) : null
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
