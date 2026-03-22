package prm392.orderfood.androidapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import prm392.orderfood.androidapp.databinding.ItemAdminCategoryBinding;
import prm392.orderfood.domain.models.category.CategoryResponse;

public class AdminCategoryListAdapter extends RecyclerView.Adapter<AdminCategoryListAdapter.ViewHolder> {
    public interface OnDeleteCategoryClickListener {
        void onDeleteClick(CategoryResponse item);
    }

    private final List<CategoryResponse> items = new ArrayList<>();
    private final OnDeleteCategoryClickListener deleteListener;

    public AdminCategoryListAdapter(OnDeleteCategoryClickListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemAdminCategoryBinding binding = ItemAdminCategoryBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position), deleteListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void submitList(List<CategoryResponse> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAdminCategoryBinding binding;

        ViewHolder(ItemAdminCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(CategoryResponse item, OnDeleteCategoryClickListener deleteListener) {
            binding.tvCategoryName.setText(item.getName() != null ? item.getName() : "");
            binding.tvCategoryDescription.setText(item.getDescription() != null ? item.getDescription() : "");
            binding.btnDeleteCategory.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onDeleteClick(item);
                }
            });
        }
    }
}
