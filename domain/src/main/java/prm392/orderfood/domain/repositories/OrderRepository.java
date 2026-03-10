package prm392.orderfood.domain.repositories;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import prm392.orderfood.domain.models.orders.Order;
import prm392.orderfood.domain.models.orders.OrderRealTime;

public interface OrderRepository {
    Completable submitCodOrder(Order newOrder);
    Flowable<List<OrderRealTime>> getOrdersByShopId(String shopId);
    Completable updateOrderStatus(String orderId, String status);
    Flowable<List<OrderRealTime>> getOrdersByUserId(String userId);
}
