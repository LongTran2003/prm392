package prm392.orderfood.androidapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import prm392.orderfood.androidapp.R;

public class SubImageAdapter extends RecyclerView.Adapter<SubImageAdapter.ViewHolder> {
    private final List<String> imageUrls = new ArrayList<>();

    public void setImageUrls(List<String> urls) {
        imageUrls.clear();
        imageUrls.addAll(urls);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SubImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sub_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubImageAdapter.ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(imageUrls.get(position))
                .placeholder(R.drawable.bg_image_placeholder)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivSubImage);
        }
    }
}
