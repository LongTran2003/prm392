package prm392.orderfood.data.datasource.remote;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import prm392.orderfood.data.datasource.remote.modelRequest.orderFireBase.OrderFireBase;
import prm392.orderfood.data.mapper.FireBaseMapper;
import prm392.orderfood.domain.models.orders.Order;
import prm392.orderfood.domain.models.orders.OrderRealTime;
import prm392.orderfood.data.datasource.remote.api.OrderApiService;
import prm392.orderfood.domain.models.orders.BankingOrderRequest;
import prm392.orderfood.domain.models.orderItem.OrderItemModel;
import prm392.orderfood.domain.models.orderItem.OrderItem;


public class OrderDataSource {
    private final DatabaseReference databaseReference;

    private final OrderApiService orderApiService;

    @Inject
    public OrderDataSource(OrderApiService orderApiService) {
        this.databaseReference = FirebaseDatabase.getInstance().getReference("orders");
        this.orderApiService = orderApiService;
    }

    public Completable submitCodOrder(Order newOrder) {
        return Completable.defer(() -> {
            BankingOrderRequest request = new BankingOrderRequest();
            request.setShopId(newOrder.getShopId());
            request.setPaymentMethod(newOrder.getPaymentMethod());

            List<OrderItemModel> orderItems = new ArrayList<>();
            for (OrderItem item : newOrder.getOrderItems()) {
                OrderItemModel model = new OrderItemModel();
                model.setMenuItemId(item.getItem().getId());
                model.setQuantity(item.getQuantity());
                model.setPrice(item.getPrice());
                orderItems.add(model);
            }
            request.setOrderItems(orderItems);

            return orderApiService.createOrder(request)
                    .flatMapCompletable(response -> {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            return Completable.complete();
                        } else {
                            String errorMsg = "Failed to submit COD order via API";
                            if (response.errorBody() != null) {
                                errorMsg += ": " + response.errorBody().string();
                            }
                            return Completable.error(new Exception(errorMsg));
                        }
                    });
        });
    }


    public Flowable<List<OrderRealTime>> getOrdersByShopId(String shopId) {
        return orderApiService.getShopOrders(shopId)
                .toFlowable()
                .map(response -> {
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        List<prm392.orderfood.data.datasource.remote.modelResponse.OrderResponseDto> dtos = response.body().getData();
                        List<OrderRealTime> result = new ArrayList<>();
                        for (prm392.orderfood.data.datasource.remote.modelResponse.OrderResponseDto dto : dtos) {
                            OrderRealTime order = new OrderRealTime();
                            order.setFirebaseId(dto.getOrderId()); // Mapping actual orderId to firebaseId variable to preserve UI code
                            order.setShopId(dto.getShopId());
                            order.setCustomerName(dto.getCustomerName());
                            order.setPaymentMethod(dto.getPaymentMethod());
                            order.setTotalAmount(dto.getTotalAmount());
                            order.setOrderStatus(dto.getOrderStatus());

                            List<prm392.orderfood.domain.models.orderItem.OrderItemRealTime> items = new ArrayList<>();
                            if (dto.getOrderItems() != null) {
                                for (prm392.orderfood.data.datasource.remote.modelResponse.OrderResponseDto.OrderItemResponseDto itemDto : dto.getOrderItems()) {
                                    prm392.orderfood.domain.models.orderItem.OrderItemRealTime item = new prm392.orderfood.domain.models.orderItem.OrderItemRealTime();
                                    item.setItemId(itemDto.getMenuItemId());
                                    item.setItemName(itemDto.getMenuItemName());
                                    item.setQuantity(itemDto.getQuantity());
                                    item.setPrice(itemDto.getPrice());
                                    items.add(item);
                                }
                            }
                            order.setOrderItems(items);
                            result.add(order);
                        }
                        return result;
                    } else {
                        throw new Exception("Failed to fetch shop orders from API");
                    }
                });
    }

    public Completable updateOrderStatus(String firebaseId, String newStatus) {
        return Completable.defer(() -> {
            prm392.orderfood.domain.models.orders.UpdateOrderStatusRequest request = new prm392.orderfood.domain.models.orders.UpdateOrderStatusRequest(newStatus);
            return orderApiService.updateOrderStatus(firebaseId, request)
                    .flatMapCompletable(response -> {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            return Completable.complete();
                        } else {
                            return Completable.error(new Exception("Failed to update order status via API"));
                        }
                    });
        });
    }

    public Flowable<List<OrderRealTime>> getOrdersByUserId(String userId) {
        return orderApiService.getCustomerOrders(userId)
                .toFlowable()
                .map(response -> {
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        List<prm392.orderfood.data.datasource.remote.modelResponse.OrderResponseDto> dtos = response.body().getData();
                        List<OrderRealTime> result = new ArrayList<>();
                        for (prm392.orderfood.data.datasource.remote.modelResponse.OrderResponseDto dto : dtos) {
                            if (!"completed".equalsIgnoreCase(dto.getOrderStatus())) {
                                OrderRealTime order = new OrderRealTime();
                                order.setFirebaseId(dto.getOrderId()); // Mapping actual orderId to firebaseId variable to preserve UI code
                                order.setShopId(dto.getShopId());
                                order.setCustomerId(userId);
                                order.setCustomerName(dto.getCustomerName());
                                order.setPaymentMethod(dto.getPaymentMethod());
                                order.setTotalAmount(dto.getTotalAmount());
                                order.setOrderStatus(dto.getOrderStatus());

                                List<prm392.orderfood.domain.models.orderItem.OrderItemRealTime> items = new ArrayList<>();
                                if (dto.getOrderItems() != null) {
                                    for (prm392.orderfood.data.datasource.remote.modelResponse.OrderResponseDto.OrderItemResponseDto itemDto : dto.getOrderItems()) {
                                        prm392.orderfood.domain.models.orderItem.OrderItemRealTime item = new prm392.orderfood.domain.models.orderItem.OrderItemRealTime();
                                        item.setItemId(itemDto.getMenuItemId());
                                        item.setItemName(itemDto.getMenuItemName());
                                        item.setQuantity(itemDto.getQuantity());
                                        item.setPrice(itemDto.getPrice());
                                        items.add(item);
                                    }
                                }
                                order.setOrderItems(items);
                                result.add(order);
                            }
                        }
                        return result;
                    } else {
                        throw new Exception("Failed to fetch pending orders from API");
                    }
                });
    }

}
