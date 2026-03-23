package prm392.orderfood.androidapp.ui.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import prm392.orderfood.androidapp.databinding.ItemCategoryBinding;
import prm392.orderfood.domain.models.category.CategoriesInShopMenu;
import prm392.orderfood.domain.models.shops.Shop;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<CategoriesInShopMenu> categoryList;
    private int selectedPosition = 0;
    private final OnCategoryActionListener onCategoryActionListener;

    public CategoryAdapter(List<CategoriesInShopMenu> categoryList, OnCategoryActionListener onCategoryActionListener) {
        this.categoryList = categoryList;
        this.onCategoryActionListener = onCategoryActionListener;
    }

    public interface OnCategoryActionListener {
        void onClick(String categoryId);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCategoryBinding binding = ItemCategoryBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CategoriesInShopMenu item = categoryList.get(position);
        ItemCategoryBinding b = holder.binding;

        b.txtCategory.setText(item.getName());
//        b.imgIcon.setImageResource(item.getIconResId());

        if (position == selectedPosition) {
            b.cardView.setCardBackgroundColor(Color.parseColor("#4CAF50"));
            b.txtCategory.setTextColor(Color.WHITE);
//            b.imgIcon.setColorFilter(Color.WHITE);
        } else {
            b.cardView.setCardBackgroundColor(Color.parseColor("#F5F5F5"));
            b.txtCategory.setTextColor(Color.parseColor("#666666"));
//            b.imgIcon.setColorFilter(Color.parseColor("#666666"));
        }

        b.getRoot().setOnClickListener(v -> {
            int previous = selectedPosition;
            selectedPosition = position;
            notifyItemChanged(previous);
            notifyItemChanged(selectedPosition);

            onCategoryActionListener.onClick(item.getId());
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemCategoryBinding binding;

        public ViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setSelectedPosition(int position) {
        int previous = selectedPosition;
        selectedPosition = position;
        notifyItemChanged(previous);
        notifyItemChanged(selectedPosition);
    }

}
