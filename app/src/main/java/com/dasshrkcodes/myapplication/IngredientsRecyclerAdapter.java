package com.dasshrkcodes.myapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

public class IngredientsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE = 1;
    private final Context context;
    private final List<String> Ing_nameList;
    private final List<String> Ing_imageList;


    public IngredientsRecyclerAdapter(Context context, List<String> Ing_nameList, List<String> Ing_imageList) {
        this.context = context;
        this.Ing_nameList = Ing_nameList;
        this.Ing_imageList = Ing_imageList;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {


        ImageView imageView;
        TextView mTitle;


        public ItemViewHolder(@NonNull View itemView) {
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        int viewType = getItemViewType(i);

        switch (viewType) {
            case TYPE:
            default:

                ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;

                itemViewHolder.mTitle.setText(Ing_nameList.get(i));
                int res = context.getResources().getIdentifier(Ing_imageList.get(i), "drawable",
                        context.getPackageName());
                itemViewHolder.imageView.setImageResource(res);

        }

    }

    @Override
    public int getItemCount() {
        return Ing_nameList.size();
    }
}
