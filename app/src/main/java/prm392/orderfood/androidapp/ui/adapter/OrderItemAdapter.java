package prm392.orderfood.androidapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import prm392.orderfood.androidapp.databinding.ItemOrderItemBinding;
import prm392.orderfood.domain.models.menuItem.MenuItemResponse;
import prm392.orderfood.domain.models.orderItem.OrderItem;
import prm392.orderfood.domain.models.orderItem.OrderItemRealTime;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder>{
    private final List<OrderItemRealTime> items;
    private final List<MenuItemResponse> menuItems;

    public OrderItemAdapter(List<OrderItemRealTime> items, List<MenuItemResponse> menuItems) {
        this.items = items;
        this.menuItems = menuItems;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemOrderItemBinding binding = ItemOrderItemBinding.inflate(inflater, parent, false);
        return new OrderItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderItemRealTime item = items.get(position);

        String itemName = item.getItemName();
        if (itemName == null || itemName.isEmpty()) {
             MenuItemResponse menuItem = findItemById(item.getItemId());
             if (menuItem != null) itemName = menuItem.getName();
             else itemName = "Unknown Item";
        }

        holder.binding.tvIndex.setText((position + 1) + ". ");
        holder.binding.tvItemName.setText(itemName);
        holder.binding.tvQuantity.setText("x" + item.getQuantity());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private MenuItemResponse findItemById(String itemId) {
        for (MenuItemResponse menuItem : menuItems) {
            if (menuItem.getId().equals(itemId)) {
                return menuItem;
            }
        }
        return null;
    }

    static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        final ItemOrderItemBinding binding;

        public OrderItemViewHolder(ItemOrderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
