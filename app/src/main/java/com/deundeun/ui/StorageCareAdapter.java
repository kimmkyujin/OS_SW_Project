package com.deundeun.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.deundeun.R;
import com.deundeun.models.StorageCare;
import java.util.List;

public class StorageCareAdapter extends RecyclerView.Adapter<StorageCareAdapter.ViewHolder> {

    private List<StorageCare> careList;

    public StorageCareAdapter(List<StorageCare> careList) {
        this.careList = careList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_storage_care, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StorageCare item = careList.get(position);
        holder.tvItemName.setText(item.getItemName());
        holder.tvDescription.setText(item.getDescription());
        
        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                 .load(item.getImageUrl())
                 .centerCrop()
                 .into(holder.ivItemImage);
        } else {
            holder.ivItemImage.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    @Override
    public int getItemCount() {
        return careList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName, tvDescription;
        ImageView ivItemImage;

        ViewHolder(View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tv_item_name);
            tvDescription = itemView.findViewById(R.id.tv_description);
            ivItemImage = itemView.findViewById(R.id.iv_item_image);
        }
    }
}
