package prm392.orderfood.androidapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import prm392.orderfood.androidapp.R;
import prm392.orderfood.domain.models.shops.Shop;

public class ShopOwnerAdapter extends RecyclerView.Adapter<ShopOwnerAdapter.ViewHolder> {

    private List<Shop> shopList;
    private final OnShopActionListener actionListener;

    private static final String REMOTE_IMAGE_HOST =
            "https://food-order-system-gndtevhzdef5hwgh.southeastasia-01.azurewebsites.net";
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

    // 👇 Interface mới cho cả Edit và Delete
    public interface OnShopActionListener {
        void onEdit(Shop shop);
        void onDelete(Shop shop);
        void onShopSelected(Shop shop);
        void onOrders(Shop shop);
    }

    public ShopOwnerAdapter(List<Shop> shopList, OnShopActionListener actionListener) {
        this.shopList = shopList;
        this.actionListener = actionListener;
    }

    public void setShopList(List<Shop> shops) {
        this.shopList = shops;
        notifyDataSetChanged();
    }

    public void appendShopList(List<Shop> shops) {
        int start = shopList.size();
        shopList.addAll(shops);
        notifyItemRangeInserted(start, shops.size());
    }

    public void removeShop(Shop shop) {
        int index = shopList.indexOf(shop);
        if (index != -1) {
            shopList.remove(index);
            notifyItemRemoved(index);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shop_owner, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Shop shop = shopList.get(position);
        holder.tvName.setText(shop.getName());
        holder.tvAddress.setText(shop.getAddress());
        holder.tvStatus.setText("Trạng thái: " + shop.getStatus());
        holder.tvOpenHours.setText("Giờ mở cửa: " + shop.getOpenHours() + " - " + shop.getEndHours());

        String normalizedCoverUrl = normalizeRemoteUrl(shop.getImageUrl());
        Glide.with(holder.itemView.getContext())
                .load(normalizedCoverUrl)
                .placeholder(R.drawable.bg_image_placeholder)
                .error(R.drawable.bg_image_placeholder)
                .into(holder.ivShop);

        // 👇 Gán sự kiện cho nút Sửa và Xóa
        holder.btnEdit.setOnClickListener(v -> actionListener.onEdit(shop));
        holder.btnDelete.setOnClickListener(v -> actionListener.onDelete(shop));
        holder.btnOrders.setOnClickListener(v -> actionListener.onOrders(shop));
        holder.itemView.setOnClickListener(v -> actionListener.onShopSelected(shop));
    }

    @Override
    public int getItemCount() {
        return shopList != null ? shopList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress, tvStatus, tvOpenHours;
        ImageView ivShop;
        Button btnEdit, btnDelete, btnOrders;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvShopName);
            tvAddress = itemView.findViewById(R.id.tvShopAddress);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvOpenHours = itemView.findViewById(R.id.tvOpenHours);
            ivShop = itemView.findViewById(R.id.ivShopImage);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnOrders = itemView.findViewById(R.id.btnOrders);
        }
    }
}
