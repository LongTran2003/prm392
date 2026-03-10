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

public class OrderDataSource {
    private final DatabaseReference databaseReference;
    @Inject
    public OrderDataSource() {
        // Constructor logic if needed
        databaseReference = FirebaseDatabase.getInstance().getReference("orders");
    }

    public Completable submitCodOrder(Order newOrder) {
        return Completable.create(emitter -> {
            String orderId = databaseReference.push().getKey();
            if (orderId != null) {
                databaseReference.child(orderId).setValue(FireBaseMapper.mapToOrderItemFireBase(newOrder))
                        .addOnSuccessListener(aVoid -> {
                            emitter.onComplete();
                        })
                        .addOnFailureListener(emitter::onError);
            } else {
                emitter.onError(new Exception("Failed to generate order ID"));
            }
        });
    }

    public Flowable<List<OrderRealTime>> getOrdersByShopId(String shopId) {
        return Flowable.create(emitter -> {
            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<OrderRealTime> orders = new ArrayList<>();
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        OrderFireBase order = childSnapshot.getValue(OrderFireBase.class);
                        String firebaseId = childSnapshot.getKey();
                        if (order != null) {
                            orders.add(FireBaseMapper.mapToOrderRealTime(order, firebaseId));
                        }
                    }
                    emitter.onNext(orders);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    emitter.onError(error.toException());
                }
            };

            Query query = databaseReference.orderByChild("shopId").equalTo(shopId);
            query.addValueEventListener(listener);

            // Remove listener when Flowable is canceled
            emitter.setCancellable(() -> query.removeEventListener(listener));
        }, BackpressureStrategy.LATEST); // LATEST: giữ giá trị mới nhất nếu không xử lý kịp
    }

    public Completable updateOrderStatus(String firebaseId, String newStatus) {
        return Completable.create(emitter -> {
            if (firebaseId == null || firebaseId.isEmpty()) {
                emitter.onError(new IllegalArgumentException("Invalid Firebase ID"));
                return;
            }

            databaseReference.child(firebaseId).child("orderStatus").setValue(newStatus)
                    .addOnSuccessListener(unused -> emitter.onComplete())
                    .addOnFailureListener(emitter::onError);
        });
    }

    public Flowable<List<OrderRealTime>> getOrdersByUserId(String userId) {
        return Flowable.create(emitter -> {
            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<OrderRealTime> orders = new ArrayList<>();
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        OrderFireBase order = childSnapshot.getValue(OrderFireBase.class);
                        String firebaseId = childSnapshot.getKey();
                        if (order != null && !"completed".equalsIgnoreCase(order.getOrderStatus())) {
                            orders.add(FireBaseMapper.mapToOrderRealTime(order, firebaseId));
                        }
                    }
                    emitter.onNext(orders);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    emitter.onError(error.toException());
                }
            };

            Query query = databaseReference.orderByChild("customerId").equalTo(userId);
            query.addValueEventListener(listener);

            // Remove listener when Flowable is canceled
            emitter.setCancellable(() -> query.removeEventListener(listener));
        }, BackpressureStrategy.LATEST); // LATEST: giữ giá trị mới nhất nếu không xử lý kịp
    }

}
