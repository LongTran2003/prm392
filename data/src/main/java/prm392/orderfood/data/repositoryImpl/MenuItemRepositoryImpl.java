package prm392.orderfood.data.repositoryImpl;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import prm392.orderfood.data.datasource.remote.MenuItemDataSource;
import prm392.orderfood.domain.models.menuItem.MenuItem;
import prm392.orderfood.domain.models.menuItem.MenuItemResponse;
import prm392.orderfood.domain.repositories.MenuItemRepository;
import retrofit2.Response;

public class MenuItemRepositoryImpl implements MenuItemRepository {
    private final MenuItemDataSource menuItemDataSource;

    @Inject
    public MenuItemRepositoryImpl(MenuItemDataSource menuItemDataSource) {
        this.menuItemDataSource = menuItemDataSource;
    }

    @Override
    public Single<Response<MenuItemResponse>> createMenuItem(MenuItem menuItem, File img) {
        return menuItemDataSource.createMenuItem(menuItem, img)
                .map(response -> {
                    if(response.isSuccess()) {
                        return Response.success(response.getData());
                    } else {
                        String errorMessage = response.getMessage() != null ? response.getMessage() : "Failed to create menu item";
                        return Response.error(400, okhttp3.ResponseBody.create(errorMessage, null));
                    }
                });
    }

    @Override
    public Single<MenuItemResponse> updateMenuItem(String id, MenuItem menuItem, File img) {
        return menuItemDataSource.updateMenuItem(id, menuItem, img)
                .map(response -> {
                    if(response.isSuccess()) {
                        return response.getData();
                    } else {
                        String errorMessage = response.getMessage() != null ? response.getMessage() : "Failed to update menu item";
                        throw new RuntimeException(errorMessage);
                    }
                });

    }

    @Override
    public Single<String> deleteMenuItem(String menuItemId) {
        return menuItemDataSource.deleteMenuItem(menuItemId)
                .map(response -> {
                    if(response.isSuccess()) {
                        return "Delete menu item successfully";
                    } else {
                        String errorMessage = response.getMessage() != null ? response.getMessage() : "Failed to delete menu item";
                        throw new RuntimeException(errorMessage);
                    }
                });
    }

    @Override
    public Single<List<MenuItemResponse>> getAllMenuItems() {
        return menuItemDataSource.getAllMenuItems()
                .map(response -> {
                    if(response.isSuccess()) {
                        return response.getData();
                    } else {
                        String errorMessage = response.getMessage() != null ? response.getMessage() : "Failed to fetch menu items";
                        throw new RuntimeException(errorMessage);
                    }
                });
    }


}
