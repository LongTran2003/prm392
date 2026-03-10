package prm392.orderfood.domain.repositories;

import java.io.File;
import java.util.List;

import io.reactivex.Single;
import prm392.orderfood.domain.models.menuItem.MenuItem;
import prm392.orderfood.domain.models.menuItem.MenuItemResponse;
import retrofit2.Response;

public interface MenuItemRepository {
    Single<Response<MenuItemResponse>> createMenuItem(MenuItem menuItem, File img);
    Single<MenuItemResponse> updateMenuItem(String id, MenuItem menuItem, File img);
    Single<String> deleteMenuItem(String menuItemId);
    Single<List<MenuItemResponse>> getAllMenuItems();
}
