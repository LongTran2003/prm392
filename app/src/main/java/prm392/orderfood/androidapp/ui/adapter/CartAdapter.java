package prm392.orderfood.androidapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import prm392.orderfood.androidapp.R;
import prm392.orderfood.androidapp.databinding.ItemFoodBinding;
import prm392.orderfood.androidapp.utils.CurrencyUtils;
import prm392.orderfood.domain.models.orderItem.OrderItem;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface OnCartActionListener {
        void onIncreaseQuantity(int position);
        void onDecreaseQuantity(int position);
        void onRemoveItem(int position);
    }

    private List<OrderItem> cartItems;
    private OnCartActionListener listener;

    public CartAdapter(List<OrderItem> cartItems, OnCartActionListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFoodBinding binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartViewHolder(binding);    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.bind(cartItems.get(position));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private final ItemFoodBinding binding;

        public CartViewHolder(ItemFoodBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.btnPlus.setOnClickListener(v -> {
                if (listener != null) listener.onIncreaseQuantity(getBindingAdapterPosition());
            });

            binding.btnMinus.setOnClickListener(v -> {
                if (listener != null) listener.onDecreaseQuantity(getBindingAdapterPosition());
            });

            binding.removeItemButton.setOnClickListener(v -> {
                if (listener != null) listener.onRemoveItem(getBindingAdapterPosition());
            });
        }

        public void bind(OrderItem item) {
            binding.foodNameTextView.setText(item.getItem().getName());
            binding.foodDescriptionTextView.setText(item.getItem().getDescription());
            binding.foodPriceTextView.setText(CurrencyUtils.formatToVND(item.getItem().getPrice()));
            binding.tvQuantity.setText(String.valueOf(item.getQuantity()));
            Glide.with(binding.getRoot())
                    .load(item.getItem().getImageUrl())
                    .placeholder(R.drawable.bg_image_placeholder)
                    .into(binding.foodImageView);
        }
    }
    public List<OrderItem> getCartItems() {
        return cartItems;
    }

    public void updateList(List<OrderItem> newList) {
        cartItems = newList;
        notifyDataSetChanged();
    }



}
