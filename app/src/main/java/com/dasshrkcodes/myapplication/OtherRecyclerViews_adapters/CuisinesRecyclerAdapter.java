package com.dasshrkcodes.myapplication.OtherRecyclerViews_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dasshrkcodes.myapplication.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class CuisinesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE = 1;
    private final Context context;
    private final List<String> Cuisine_nameList;
    private final List<String> Cuisine_imageList;
    private ingredients_click_RecyclerView mCallback;


    public CuisinesRecyclerAdapter(Context context, List<String> Cuisine_nameList, List<String> Cuisine_imageList, ingredients_click_RecyclerView mCallback) {
        this.context = context;
        this.Cuisine_nameList = Cuisine_nameList;
        this.Cuisine_imageList = Cuisine_imageList;
        this.mCallback = mCallback;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {


        ImageView imageView;
        TextView mTitle;
        MaterialCardView cuisine_card;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.cuisine_image);
            mTitle = itemView.findViewById(R.id.cuisine_text);
            cuisine_card = itemView.findViewById(R.id.cuisine_card);

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case TYPE:

            default:

                View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.cuisine_card, viewGroup, false);

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

                itemViewHolder.mTitle.setText(Cuisine_nameList.get(i));
                int res = context.getResources().getIdentifier(Cuisine_imageList.get(i), "drawable",
                        context.getPackageName());
                itemViewHolder.imageView.setImageResource(res);

                itemViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (itemViewHolder.cuisine_card.getStrokeWidth() > 0) {
                            itemViewHolder.cuisine_card.setStrokeWidth(0);
                        } else {
                            itemViewHolder.cuisine_card.setStrokeWidth(8);
                        }
                        mCallback.onClick(Cuisine_nameList.get(i));

                    }
                });

                itemViewHolder.cuisine_card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (itemViewHolder.cuisine_card.getStrokeWidth() > 0) {
                            itemViewHolder.cuisine_card.setStrokeWidth(0);
                        } else {
                            itemViewHolder.cuisine_card.setStrokeWidth(8);
                        }
                        mCallback.onClick(Cuisine_nameList.get(i));

                    }
                });

        }

    }

    @Override
    public int getItemCount() {
        return Cuisine_nameList.size();
    }
}
