package com.dasshrkcodes.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HorizontalRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE = 1;
    private final Context context;
    private final List<Recipes> listRecyclerItem;



    public HorizontalRecyclerAdapter(Context context, List<Recipes> listRecyclerItem) {
        this.context = context;
        this.listRecyclerItem = listRecyclerItem;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {



        ImageView imageView;
        TextView mTitle, mDescription;
        MaterialCardView small_card;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.smallR_featured_image);
            mTitle = itemView.findViewById(R.id.smallR_featured_title);
            mDescription = itemView.findViewById(R.id.smallR_featured_desc);
            small_card = itemView.findViewById(R.id.small_card);

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case TYPE:

                default:

                    View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                            R.layout.featured_card_design, viewGroup, false);

                    return new ItemViewHolder((layoutView));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        int viewType = getItemViewType(i);

        switch (viewType) {
            case TYPE:
                default:

                    ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
                    Recipes recipes = (Recipes) listRecyclerItem.get(i);

                    itemViewHolder.mTitle.setText(recipes.getRName());
                    itemViewHolder.mDescription.setText(recipes.getInstructions());
                    itemViewHolder.small_card.setTag(recipes.getId());

                    Picasso.get().load(recipes.getImgUrl()).into(itemViewHolder.imageView);

        }

    }

    @Override
    public int getItemCount() {
        return listRecyclerItem.size();
    }
}
