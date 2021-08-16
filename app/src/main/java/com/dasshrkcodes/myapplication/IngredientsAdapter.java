package com.dasshrkcodes.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE = 1;
    private final Context context;
    private final List<Recipes> listRecyclerItem;

    public IngredientsAdapter(Context context, List<Recipes> listRecyclerItem) {
        this.context = context;
        this.listRecyclerItem = listRecyclerItem;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView mTitle;

        public ItemViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.ing_image);
            mTitle = itemView.findViewById(R.id.ing_text);

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case TYPE:
            default:

                View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.ingredient_card, viewGroup, false);

                return new ItemViewHolder((layoutView));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int i) {

        int viewType = getItemViewType(i);

        switch (viewType) {
            case TYPE:
            default:

                RecyclerAdapter.ItemViewHolder itemViewHolder = (RecyclerAdapter.ItemViewHolder) viewHolder;
                Recipes recipes = (Recipes) listRecyclerItem.get(i);

                itemViewHolder.mTitle.setText(recipes.getRName());
                Picasso.get().load(recipes.getImgUrl()).into(itemViewHolder.imageView);
        }

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
