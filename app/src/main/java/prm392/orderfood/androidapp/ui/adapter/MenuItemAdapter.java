package prm392.orderfood.androidapp.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Locale;
import java.util.List;

import prm392.orderfood.androidapp.R;
import prm392.orderfood.androidapp.databinding.ItemMenuBinding;
import prm392.orderfood.androidapp.utils.CurrencyUtils;
import prm392.orderfood.domain.models.menuItem.MenuItemResponse;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {
    private List<MenuItemResponse> items;
    private String userRole;
    private final OnMenuItemActionListener onMenuItemActionListener;
    private static final String REMOTE_IMAGE_HOST =
            "https://food-order-system-gndtevhzdef5hwgh.southeastasia-01.azurewebsites.net";

    public MenuItemAdapter(List<MenuItemResponse> items, String userRole, OnMenuItemActionListener onMenuItemActionListener) {
        this.items = items;
        this.userRole = userRole;
        this.onMenuItemActionListener = onMenuItemActionListener;
    }

    private static String normalizeRole(String role) {
        if (role == null) return "";
        return role.replace("_", "")
                .replace("-", "")
                .replace(" ", "")
                .trim()
                .toLowerCase(Locale.ROOT);
    }

    private String normalizeRemoteUrl(String rawUrl) {
        if (rawUrl == null) return null;
        String url = rawUrl.trim();
        if (url.isEmpty() || "null".equalsIgnoreCase(url)) return null;
        if (url.startsWith("http://")
                || url.startsWith("https://")
                || url.startsWith("content://")
                || url.startsWith("file://")) {
            return url;
        }
        if (!url.startsWith("/")) {
            url = "/" + url;
        }
        return REMOTE_IMAGE_HOST + url;
    }

        private boolean isShopOwnerRole() {
        return "shopowner".equals(normalizeRole(userRole));
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
        notifyDataSetChanged();
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

        String normalizedItemImageUrl = normalizeRemoteUrl(item.getImageUrl());
        Glide.with(holder.itemView.getContext())
                .load(normalizedItemImageUrl)
                .placeholder(R.drawable.bg_image_placeholder)
                .error(R.drawable.bg_image_placeholder)
                .centerCrop()
                .into(holder.binding.imgItem);

        //Ẩn hiện nút Add to Cart theo role
        if (isShopOwnerRole()) {
            holder.binding.cvAddToCart.setVisibility(View.GONE); // Ẩn
        } else {
            holder.binding.cvAddToCart.setVisibility(View.VISIBLE); // Hiện
        }

        //Ẩn hiện nút Edit và Delete theo role
        if (isShopOwnerRole()) {
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
