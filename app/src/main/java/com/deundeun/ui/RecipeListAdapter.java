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
import com.deundeun.models.Recipe;
import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {

    private List<Recipe> recipes;
    private OnItemClickListener listener;
    private boolean isGridView = false;

    public interface OnItemClickListener {
        void onItemClick(Recipe recipe);
        void onHeartClick(Recipe recipe);
    }

    public RecipeListAdapter(List<Recipe> recipes, OnItemClickListener listener) {
        this.recipes = recipes;
        this.listener = listener;
    }

    public void setGridView(boolean gridView) {
        isGridView = gridView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = isGridView ? R.layout.item_recipe_grid : R.layout.item_recipe;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.tvTitle.setText(recipe.getTitle());
        
        if (isGridView && holder.ivHeart != null) {
            holder.ivHeart.setVisibility(View.VISIBLE);
            holder.ivHeart.setColorFilter(android.graphics.Color.BLACK);
            holder.ivHeart.setOnClickListener(v -> listener.onHeartClick(recipe));
        } else if (holder.ivHeart != null) {
            holder.ivHeart.setVisibility(View.GONE);
        }

        if (recipe.getImageUrl() != null && !recipe.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                 .load(recipe.getImageUrl())
                 .centerCrop()
                 .into(holder.ivThumb);
        } else {
            holder.ivThumb.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(recipe));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumb;
        TextView tvTitle;
        ImageView ivHeart;

        ViewHolder(View itemView) {
            super(itemView);
            ivThumb = itemView.findViewById(R.id.iv_recipe_thumb);
            tvTitle = itemView.findViewById(R.id.tv_recipe_title);
            ivHeart = itemView.findViewById(R.id.iv_heart);
        }
    }
}
