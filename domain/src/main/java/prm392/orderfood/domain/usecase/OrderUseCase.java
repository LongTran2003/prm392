package prm392.orderfood.domain.usecase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import prm392.orderfood.domain.models.orders.Order;
import prm392.orderfood.domain.models.orders.OrderRealTime;
import prm392.orderfood.domain.repositories.OrderRepository;

public class OrderUseCase {
    private final OrderRepository orderRepository;

    @Inject
    public OrderUseCase(OrderRepository orderRepository) {
        // Constructor logic if needed
        this.orderRepository = orderRepository;
    }

    public Completable submitCodOrder(Order newOrder) {
        // Logic to submit a cash on delivery order
        return orderRepository.submitCodOrder(newOrder)
                .subscribeOn(Schedulers.io());
    }

    public Flowable<List<OrderRealTime>> getOrdersByShopId(String shopId) {
        // Logic to get orders by shop ID
        return orderRepository.getOrdersByShopId(shopId)
                .subscribeOn(Schedulers.io());
    }

    public Completable updateOrderStatus(String orderId, String status) {
        // Logic to update the status of an order
        return orderRepository.updateOrderStatus(orderId, status)
                .subscribeOn(Schedulers.io());
    }

    public Flowable<List<OrderRealTime>> getOrdersByUserId(String userId) {
        // Logic to get orders by user ID
        return orderRepository.getOrdersByUserId(userId)
                .subscribeOn(Schedulers.io());
    }
}
