package prm392.orderfood.androidapp.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import prm392.orderfood.androidapp.R;
import prm392.orderfood.androidapp.databinding.ItemOrderBinding;
import prm392.orderfood.androidapp.utils.CurrencyUtils;
import prm392.orderfood.domain.models.menuItem.MenuItemResponse;
import prm392.orderfood.domain.models.orders.OrderRealTime;
import prm392.orderfood.domain.models.users.CustomerResponse;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private final List<OrderRealTime> orders;
    private final List<MenuItemResponse> menuItems; // danh sách món từ ShopViewModel
    private final List<CustomerResponse> customers; // danh sách khách hàng từ UserViewModel
    private final Set<String> highlightedOrders = new HashSet<>();
    private OnOrderActionListener listener;

    public interface OnOrderActionListener {
        void onConfirmClicked(OrderRealTime order);
        void onCancelClicked(OrderRealTime order);
        void onDeliveredClicked(OrderRealTime order);
        void onDoneClicked(OrderRealTime order);
    }

    public OrderAdapter(List<OrderRealTime> orders, List<MenuItemResponse> menuItems, OnOrderActionListener listener, List<CustomerResponse> customers) {
        this.orders = orders;
        this.menuItems = menuItems;
        this.listener = listener;
        this.customers = customers;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemOrderBinding binding = ItemOrderBinding.inflate(inflater, parent, false);
        return new OrderViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderRealTime order = orders.get(position);

        String cName = order.getCustomerName() != null ? order.getCustomerName() : "Unknown Customer";
        holder.binding.tvCustomerId.setText("Customer: " + cName);
        holder.binding.tvCustomerPhone.setVisibility(View.GONE);
        holder.binding.tvPaymentMethod.setText("PaymentMethod: " + order.getPaymentMethod());
        holder.binding.tvTotalAmount.setText("Total Amount: " + CurrencyUtils.formatToVND(order.getTotalAmount()));
        holder.binding.tvOrderStatus.setText("Status: " + order.getOrderStatus());
        // Nested RecyclerView
        OrderItemAdapter itemAdapter = new OrderItemAdapter(order.getOrderItems(), menuItems);
        holder.binding.rvOrderItems.setAdapter(itemAdapter);

        // Highlight
        if (highlightedOrders.contains(order.getFirebaseId())) {
            holder.binding.getRoot().setBackgroundColor(0xFFE0F7FA); // Màu xanh nhạt

            holder.binding.getRoot().postDelayed(() -> {
                int currentPos = holder.getBindingAdapterPosition();
                if (currentPos != RecyclerView.NO_POSITION) {
                    String currentId = orders.get(currentPos).getFirebaseId();
                    highlightedOrders.remove(currentId);
                    notifyItemChanged(currentPos); // Gọi lại chính item đó để vẽ lại màu
                }
            }, 3000);
        } else {
            holder.binding.getRoot().setBackgroundColor(0xFFFFFFFF);
        }

        // Reset visibility về GONE trước khi kiểm tra
        holder.binding.btnConfirm.setVisibility(View.GONE);
        holder.binding.btnCancel.setVisibility(View.GONE);
        holder.binding.btnDelivered.setVisibility(View.GONE);
        holder.binding.btnDone.setVisibility(View.GONE);

        // Hiển thị nút theo trạng thái
        String safeStatus = order.getOrderStatus() != null ? order.getOrderStatus().toLowerCase() : "";
        switch (safeStatus) {
            case "pending":
                holder.binding.tvOrderStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_orange_dark));
                holder.binding.btnConfirm.setVisibility(View.VISIBLE);
                holder.binding.btnCancel.setVisibility(View.VISIBLE);
                break;
            case "confirmed":
                holder.binding.tvOrderStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.teal_700));
                holder.binding.btnDelivered.setVisibility(View.VISIBLE);
                break;
            case "delivered":
                holder.binding.tvOrderStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.primary_color));
                holder.binding.btnDone.setVisibility(View.VISIBLE);
                break;
            case "completed":
                holder.binding.tvOrderStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_green_dark));
                break;

            case "cancelled":
                holder.binding.tvOrderStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_red_dark));
                break;

            default:
                holder.binding.tvOrderStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.darker_gray));
                break;
        }

        holder.binding.btnConfirm.setOnClickListener(v -> {
            if (listener != null) listener.onConfirmClicked(order);
        });

        holder.binding.btnCancel.setOnClickListener(v -> {
            if (listener != null) listener.onCancelClicked(order);
        });

        holder.binding.btnDelivered.setOnClickListener(v -> {
            if (listener != null) listener.onDeliveredClicked(order);
        });

        holder.binding.btnDone.setOnClickListener(v -> {
            if (listener != null) listener.onDoneClicked(order);
        });

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void updateOrders(List<OrderRealTime> newOrders) {
        for (OrderRealTime newOrder : newOrders) {
            String id = newOrder.getFirebaseId();
            int existingIndex = findIndexById(id);

            if (existingIndex == -1) {
                // New order -> insert on top
                orders.add(0, newOrder);
                highlightedOrders.add(id);
                notifyItemInserted(0);
            } else if (!newOrder.equals(orders.get(existingIndex))) {
                // Updated order
                orders.set(existingIndex, newOrder);
                highlightedOrders.add(id);
                notifyItemChanged(existingIndex);
            }
        }
    }

    private int findIndexById(String firebaseId) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getFirebaseId().equals(firebaseId)) {
                return i;
            }
        }
        return -1;
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        final ItemOrderBinding binding;

        public OrderViewHolder(ItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
