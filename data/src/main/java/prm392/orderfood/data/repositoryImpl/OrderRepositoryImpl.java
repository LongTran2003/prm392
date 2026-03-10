package prm392.orderfood.data.repositoryImpl;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import prm392.orderfood.data.datasource.remote.MenuItemDataSource;
import prm392.orderfood.data.datasource.remote.OrderDataSource;
import prm392.orderfood.domain.models.orders.Order;
import prm392.orderfood.domain.models.orders.OrderRealTime;
import prm392.orderfood.domain.repositories.OrderRepository;

public class OrderRepositoryImpl implements OrderRepository {
    private final OrderDataSource orderDataSource;

    @Inject
    public OrderRepositoryImpl(OrderDataSource orderDataSource) {
        this.orderDataSource = orderDataSource;
    }


    @Override
    public Completable submitCodOrder(Order newOrder) {
        return orderDataSource.submitCodOrder(newOrder);
    }

    @Override
    public Flowable<List<OrderRealTime>> getOrdersByShopId(String shopId) {
        return orderDataSource.getOrdersByShopId(shopId);
    }

    @Override
    public Completable updateOrderStatus(String orderId, String status) {
        return orderDataSource.updateOrderStatus(orderId, status);
    }

    @Override
    public Flowable<List<OrderRealTime>> getOrdersByUserId(String userId) {
        return orderDataSource.getOrdersByUserId(userId);
    }


}
