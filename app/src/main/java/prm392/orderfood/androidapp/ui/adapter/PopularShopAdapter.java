package prm392.orderfood.androidapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import prm392.orderfood.androidapp.R;
import prm392.orderfood.androidapp.databinding.ItemShopGridBinding;
import prm392.orderfood.domain.models.shops.PopularShopResponse;

public class PopularShopAdapter  extends RecyclerView.Adapter<PopularShopAdapter.ShopViewHolder>{

    private List<PopularShopResponse> shopList;
    private final OnShopClickListener listener;

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

    public interface OnShopClickListener {
        void onShopClick(PopularShopResponse shop);
    }

    public PopularShopAdapter(List<PopularShopResponse> shopList, OnShopClickListener listener) {
        this.shopList = shopList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemShopGridBinding binding = ItemShopGridBinding.inflate(inflater, parent, false);
        return new ShopViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        PopularShopResponse shop = shopList.get(position);
        holder.bind(shop);
    }

    @Override
    public int getItemCount() {
        return shopList != null ? shopList.size() : 0;
    }

    public void updateData(List<PopularShopResponse> newList) {
        this.shopList = newList;
        notifyDataSetChanged();
    }

    class ShopViewHolder extends RecyclerView.ViewHolder {
        private final ItemShopGridBinding binding;

        public ShopViewHolder(ItemShopGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(PopularShopResponse shop) {
            binding.tvShopName.setText(shop.getName());
            binding.tvShopDesc.setText(shop.getAddress());
            binding.tvRating.setText(String.valueOf(shop.getRating()));

            String normalizedCoverUrl = normalizeRemoteUrl(shop.getImageUrl());
            Glide.with(binding.getRoot().getContext())
                    .load(normalizedCoverUrl)
                    .placeholder(R.drawable.bg_image_placeholder)
                    .error(R.drawable.bg_image_placeholder)
                    .override(Target.SIZE_ORIGINAL)
                    .centerCrop()
                    .into(binding.ivShopImage);

            binding.getRoot().setOnClickListener(v -> listener.onShopClick(shop));
        }
    }
}
