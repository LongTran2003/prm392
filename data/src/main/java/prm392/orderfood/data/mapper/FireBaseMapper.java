package prm392.orderfood.data.mapper;

import java.util.ArrayList;

import prm392.orderfood.data.datasource.remote.modelRequest.orderFireBase.OrderFireBase;
import prm392.orderfood.data.datasource.remote.modelRequest.orderFireBase.OrderItemFireBase;
import prm392.orderfood.domain.models.orderItem.OrderItem;
import prm392.orderfood.domain.models.orderItem.OrderItemRealTime;
import prm392.orderfood.domain.models.orders.Order;
import prm392.orderfood.domain.models.orders.OrderRealTime;

public class FireBaseMapper {
    public static OrderFireBase mapToOrderItemFireBase(Order order) {
        OrderFireBase orderFB = new OrderFireBase();
        orderFB.setCustomerId(order.getCustomerId());
        orderFB.setShopId(order.getShopId());
        orderFB.setPaymentMethod(order.getPaymentMethod());
        orderFB.setTotalAmount(order.getTotalAmount());
        orderFB.setOrderStatus(order.getOrderStatus());

        // Map order items
        var orderItems = new ArrayList<OrderItemFireBase>();
        for (OrderItem item : order.getOrderItems()) {
            orderItems.add(mapToOrderItemFireBase(item));
        }

        orderFB.setOrderItems(orderItems);
        return orderFB;
    }

    public static OrderItemFireBase mapToOrderItemFireBase(OrderItem orderItem) {
        OrderItemFireBase orderItemFireBase = new OrderItemFireBase();
        orderItemFireBase.setItemId(orderItem.getItem().getId());
        orderItemFireBase.setQuantity(orderItem.getQuantity());
        orderItemFireBase.setPrice(orderItem.getPrice());
        return orderItemFireBase;
    }

    public static OrderRealTime mapToOrderRealTime(OrderFireBase orderFireBase, String firebaseId) {
        OrderRealTime orderRealTime = new OrderRealTime();
        orderRealTime.setFirebaseId(firebaseId);
        orderRealTime.setCustomerId(orderFireBase.getCustomerId());
        orderRealTime.setShopId(orderFireBase.getShopId());
        orderRealTime.setPaymentMethod(orderFireBase.getPaymentMethod());
        orderRealTime.setTotalAmount(orderFireBase.getTotalAmount());
        orderRealTime.setOrderStatus(orderFireBase.getOrderStatus());

        // Map order items
        var orderItems = new ArrayList<OrderItemRealTime>();
        for (OrderItemFireBase item : orderFireBase.getOrderItems()) {
            orderItems.add(mapToOrderItemRealTime(item));
        }
        orderRealTime.setOrderItems(orderItems);

        return orderRealTime;
    }

    public static OrderItemRealTime mapToOrderItemRealTime(OrderItemFireBase orderItemFireBase) {
        OrderItemRealTime orderItemRealTime = new OrderItemRealTime();
        orderItemRealTime.setItemId(orderItemFireBase.getItemId());
        orderItemRealTime.setQuantity(orderItemFireBase.getQuantity());
        orderItemRealTime.setPrice(orderItemFireBase.getPrice());
        return orderItemRealTime;
    }
}
