package prm392.orderfood.androidapp.ui.fragment.admin;

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

public class ShopApprovalAdapter extends RecyclerView.Adapter<ShopApprovalAdapter.ShopViewHolder> {

    private List<Shop> shopList;
    private final String status;
    private final OnActionClickListener approveListener;
    private final OnActionClickListener rejectListener;
    private static final String REMOTE_IMAGE_HOST =
            "https://food-order-system-gndtevhzdef5hwgh.southeastasia-01.azurewebsites.net";
    public interface OnActionClickListener {
        void onClick(Shop shop);
    }

    public ShopApprovalAdapter(List<Shop> shopList,
                               String status,
                               OnActionClickListener approveListener,
                               OnActionClickListener rejectListener) {
        this.shopList = shopList;
        this.status = status;
        this.approveListener = approveListener;
        this.rejectListener = rejectListener;
    }

    public void setShopList(List<Shop> shops) {
        this.shopList = shops;
        notifyDataSetChanged();
    }

    public void appendShopList(List<Shop> newShops) {
        int start = shopList.size();
        shopList.addAll(newShops);
        notifyItemRangeInserted(start, newShops.size());
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shop_approval, parent, false);
        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        Shop shop = shopList.get(position);

        holder.tvName.setText(shop.getName());
        holder.tvAddress.setText(shop.getAddress());
        holder.tvOwner.setText("Chủ shop: " + (shop.getOwner() != null ? shop.getOwner().getFullName() : "Không rõ"));
        holder.tvOpenHours.setText("Giờ mở cửa: " + shop.getOpenHours() + " - " + shop.getEndHours());
        holder.tvRating.setText("Đánh giá: ⭐ " + shop.getRating());
        holder.tvStatus.setText("Trạng thái: " + shop.getStatus());

        // Load ảnh shop
        String normalizedImageUrl = normalizeRemoteUrl(shop.getImageUrl());
        Glide.with(holder.itemView.getContext())
                .load(normalizedImageUrl)
                .placeholder(R.drawable.bg_image_placeholder)
                .error(R.drawable.bg_image_placeholder)
                .into(holder.ivImage);

        // Chỉ hiển thị nút khi là tab "Pending"
        if ("Pending".equalsIgnoreCase(status)) {
            holder.btnApprove.setVisibility(View.VISIBLE);
            holder.btnReject.setVisibility(View.VISIBLE);
            holder.btnApprove.setOnClickListener(v -> approveListener.onClick(shop));
            holder.btnReject.setOnClickListener(v -> rejectListener.onClick(shop));
        } else {
            holder.btnApprove.setVisibility(View.GONE);
            holder.btnReject.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return shopList != null ? shopList.size() : 0;
    }

    public static class ShopViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress, tvOwner, tvOpenHours, tvRating, tvStatus;
        ImageView ivImage;
        Button btnApprove, btnReject;

        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivShopImage);
            tvName = itemView.findViewById(R.id.tvShopName);
            tvAddress = itemView.findViewById(R.id.tvShopAddress);
            tvOwner = itemView.findViewById(R.id.tvShopOwner);
            tvOpenHours = itemView.findViewById(R.id.tvOpenHours);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
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
}

