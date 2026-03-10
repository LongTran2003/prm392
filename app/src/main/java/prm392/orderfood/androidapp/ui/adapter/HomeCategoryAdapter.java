package prm392.orderfood.androidapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import prm392.orderfood.androidapp.databinding.ItemCategoryBinding;
import prm392.orderfood.androidapp.databinding.ItemCategoryHorizontalBinding;
import prm392.orderfood.domain.models.category.CategoryResponse;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder>{
    private List<CategoryResponse> categoryList;
    private int selectedPosition = 0;
    private final HomeCategoryAdapter.OnCategoryActionListener onCategoryActionListener;

    public HomeCategoryAdapter(List<CategoryResponse> categoryList, HomeCategoryAdapter.OnCategoryActionListener onCategoryActionListener) {
        this.categoryList = categoryList;
        this.onCategoryActionListener = onCategoryActionListener;
    }

    public interface OnCategoryActionListener {
        void onCategoryClick(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCategoryHorizontalBinding binding = ItemCategoryHorizontalBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryResponse category = categoryList.get(position);
        boolean isSelected = position == selectedPosition;

        holder.bind(category, isSelected);

        holder.binding.getRoot().setOnClickListener(v -> {
            int clickedPosition = holder.getAdapterPosition();
            if (clickedPosition == RecyclerView.NO_POSITION) return;

            int previousSelected = selectedPosition;
            selectedPosition = clickedPosition;

            // Only notify if position actually changed
            if (previousSelected != selectedPosition) {
                notifyItemChanged(previousSelected);
                notifyItemChanged(selectedPosition);
            }

            onCategoryActionListener.onCategoryClick(selectedPosition);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList != null ? categoryList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemCategoryHorizontalBinding binding;

        public ViewHolder(ItemCategoryHorizontalBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CategoryResponse item, boolean isSelected) {
            binding.tvCategoryName.setText(item.getName());

            // Highlight selected
            if (isSelected) {
                binding.cvCategory.setCardBackgroundColor(0xFFFF8F00); // Orange
            } else {
                binding.cvCategory.setCardBackgroundColor(0xFFFFFFFF); // White
            }
        }
    }

    public void updateData(List<CategoryResponse> newList) {
        this.categoryList.clear();
        this.categoryList.addAll(newList);
        notifyDataSetChanged();
    }

    public CategoryResponse getSelectedCategory() {
        if (categoryList == null || selectedPosition < 0 || selectedPosition >= categoryList.size()) return null;
        return categoryList.get(selectedPosition);
    }
}
