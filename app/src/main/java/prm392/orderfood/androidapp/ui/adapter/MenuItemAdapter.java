package prm392.orderfood.androidapp.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import prm392.orderfood.androidapp.R;
import prm392.orderfood.androidapp.databinding.ItemMenuBinding;
import prm392.orderfood.androidapp.utils.CurrencyUtils;
import prm392.orderfood.domain.models.menuItem.MenuItemResponse;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {
    private List<MenuItemResponse> items;
    private String userRole;
    private final OnMenuItemActionListener onMenuItemActionListener;

    public MenuItemAdapter(List<MenuItemResponse> items, String userRole, OnMenuItemActionListener onMenuItemActionListener) {
        this.items = items;
        this.userRole = userRole;
        this.onMenuItemActionListener = onMenuItemActionListener;
    }

    public interface OnMenuItemActionListener {
        void onUpdate(MenuItemResponse item);
        void onDelete(MenuItemResponse item);
        void onClick(MenuItemResponse item);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMenuBinding binding = ItemMenuBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuItemResponse item = items.get(position);
        if (item == null) {
            return;
        }

        holder.binding.tvName.setText(item.getName());
        holder.binding.tvDescription.setText(item.getDescription());
        holder.binding.tvPrice.setText(CurrencyUtils.formatToVND(item.getPrice()));
        holder.binding.tvDescription.setText(item.getDescription());

        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .placeholder(R.drawable.highland_americano)
                .into(holder.binding.imgItem);

        //Ẩn hiện nút Add to Cart theo role
        if ("ShopOwner".equalsIgnoreCase(userRole)) {
            holder.binding.cvAddToCart.setVisibility(View.GONE); // Ẩn
        } else {
            holder.binding.cvAddToCart.setVisibility(View.VISIBLE); // Hiện
        }

        //Ẩn hiện nút Edit và Delete theo role
        if ("ShopOwner".equalsIgnoreCase(userRole)) {
            holder.binding.cvUpdate.setVisibility(View.VISIBLE); // Hiện
            holder.binding.cvDelete.setVisibility(View.VISIBLE); // Hiện

            holder.binding.cvUpdate.setOnClickListener(v -> {
                if (onMenuItemActionListener != null) {
                    onMenuItemActionListener.onUpdate(item);
                }
            });

            holder.binding.cvDelete.setOnClickListener(v -> {
                if (onMenuItemActionListener != null) {
                    onMenuItemActionListener.onDelete(item);
                }
            });
        } else {
            holder.binding.cvUpdate.setVisibility(View.GONE); // Ẩn
            holder.binding.cvDelete.setVisibility(View.GONE); // Ẩn
        }

        holder.binding.getRoot().setOnClickListener(v -> {
            if (onMenuItemActionListener != null) {
                onMenuItemActionListener.onClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemMenuBinding binding;

        public ViewHolder(@NonNull ItemMenuBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void updateData(List<MenuItemResponse> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

}
