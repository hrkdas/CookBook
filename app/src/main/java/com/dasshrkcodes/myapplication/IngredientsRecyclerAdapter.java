package com.dasshrkcodes.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class IngredientsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE = 1;
    private final Context context;
    private final List<String> Ing_nameList;
    private final List<String> Ing_imageList;
    private ingredients_click_RecyclerView mCallback;


    public IngredientsRecyclerAdapter(Context context, List<String> Ing_nameList, List<String> Ing_imageList,ingredients_click_RecyclerView mCallback) {
        this.context = context;
        this.Ing_nameList = Ing_nameList;
        this.Ing_imageList = Ing_imageList;
        this.mCallback = mCallback;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {


        ImageView imageView;
        TextView mTitle;
        MaterialCardView ing_card;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.ing_image);
            mTitle = itemView.findViewById(R.id.ing_text);
            ing_card = itemView.findViewById(R.id.ing_card);

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

                itemViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (itemViewHolder.ing_card.getStrokeWidth() > 0) {
                            itemViewHolder.ing_card.setStrokeWidth(0);
                        } else {
                            itemViewHolder.ing_card.setStrokeWidth(5);
                        }
                        mCallback.onClick(Ing_nameList.get(i));

                    }
                });

                itemViewHolder.ing_card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (itemViewHolder.ing_card.getStrokeWidth() > 0) {
                            itemViewHolder.ing_card.setStrokeWidth(0);
                        } else {
                            itemViewHolder.ing_card.setStrokeWidth(5);
                        }
                        mCallback.onClick(Ing_nameList.get(i));

                    }
                });

        }

    }

    @Override
    public int getItemCount() {
        return Ing_nameList.size();
    }
}
