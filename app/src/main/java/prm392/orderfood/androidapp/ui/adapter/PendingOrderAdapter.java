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
import prm392.orderfood.androidapp.databinding.ItemPendingOrderBinding;
import prm392.orderfood.androidapp.utils.CurrencyUtils;
import prm392.orderfood.domain.models.menuItem.MenuItemResponse;
import prm392.orderfood.domain.models.orders.OrderRealTime;
import prm392.orderfood.domain.models.shops.PopularShopResponse;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>{
    private final List<OrderRealTime> orders;
    private final List<MenuItemResponse> menuItems;
    private final List<PopularShopResponse> shops;
    private final Set<String> highlightedOrders = new HashSet<>();

    public PendingOrderAdapter(List<OrderRealTime> orders, List<MenuItemResponse> menuItems, List<PopularShopResponse> shops) {
        this.orders = orders;
        this.menuItems = menuItems;
        this.shops = shops;
    }

    @NonNull
    @Override
    public PendingOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPendingOrderBinding binding = ItemPendingOrderBinding.inflate(inflater, parent, false);
        return new PendingOrderAdapter.PendingOrderViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PendingOrderViewHolder holder, int position) {
        OrderRealTime order = orders.get(position);
        // Filter shop details
        PopularShopResponse shop = new PopularShopResponse();
        for (PopularShopResponse s : shops) {
            if (s.getId().equals(order.getShopId())) {
                shop = s;
                break;
            }
        }

        holder.binding.tvShopId.setText("Shop: " + shop.getName());
        holder.binding.tvShopAddress.setText("Address: " + shop.getAddress());
        holder.binding.tvPaymentMethod.setText("Payment Method: " + order.getPaymentMethod());
        holder.binding.tvTotalAmount.setText("TotalAmount: " + CurrencyUtils.formatToVND(order.getTotalAmount()));
        holder.binding.tvOrderStatus.setText("Status: " + order.getOrderStatus());

        // Nested RecyclerView
        OrderItemAdapter itemAdapter = new OrderItemAdapter(order.getOrderItems(), menuItems);
        holder.binding.rvOrderItems.setAdapter(itemAdapter);

        holder.binding.btnConfirm.setVisibility(View.GONE);
        holder.binding.btnCancel.setVisibility(View.GONE);
        holder.binding.btnDelivered.setVisibility(View.GONE);
        holder.binding.btnDone.setVisibility(View.GONE);

        // Hiển thị trạng thái
        switch (order.getOrderStatus()) {
            case "Pending":
                holder.binding.tvOrderStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_orange_dark));
                break;
            case "Confirmed":
                holder.binding.tvOrderStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.teal_700));
                break;
            case "Delivered":
                holder.binding.tvOrderStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.primary_color));
                break;
            case "Completed":
                holder.binding.tvOrderStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_green_dark));
                break;

            case "Cancelled":
                holder.binding.tvOrderStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_red_dark));
                break;

            default:
                holder.binding.tvOrderStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.darker_gray));
                break;
        }

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

    static class PendingOrderViewHolder extends RecyclerView.ViewHolder {
        final ItemPendingOrderBinding binding;

        public PendingOrderViewHolder(ItemPendingOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
