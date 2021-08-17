package com.dasshrkcodes.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.card.MaterialCardView;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class search_rAdapter extends FirestoreRecyclerAdapter<Recipes, search_rAdapter.ItemViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    private final OnItemClickListener listener;
    search_rAdapter(FirestoreRecyclerOptions<Recipes> options, OnItemClickListener listener) {
        super(options);
        this.listener = listener;
    }

    search_rAdapter(FirestoreRecyclerOptions<Recipes> options) {
        super(options);
        this.listener = null;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView, bigR_veg_icon, bigR_nonveg_icon;
        LikeButton bigR_likebutton;
        TextView mTitle, mDescription, mTime,bigR_difficulty;
        MaterialCardView big_card;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.bigR_featured_image);
            mTitle = itemView.findViewById(R.id.bigR_featured_title);
            mDescription = itemView.findViewById(R.id.bigR_featured_desc);
            mTime = itemView.findViewById(R.id.bigR_cooktime);
            big_card = itemView.findViewById(R.id.big_card);
            bigR_likebutton = itemView.findViewById(R.id.bigR_likebutton);
            bigR_veg_icon = itemView.findViewById(R.id.bigR_veg_icon);
            bigR_nonveg_icon = itemView.findViewById(R.id.bigR_nonveg_icon);
            bigR_difficulty = itemView.findViewById(R.id.bigR_difficulty);

        }
    }

    private boolean IsNonveg(String cleanedIngredients, String[] cardtype) {
        boolean result = false;
        int n = cardtype.length;
        for (int i = 0; i < n; i++) {
            result = cleanedIngredients.contains(cardtype[i]);
            if (result) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onBindViewHolder(@NonNull ItemViewHolder viewHolder, int i, @NonNull Recipes recipes) {

        ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;

        itemViewHolder.mTitle.setText(recipes.getRName());
        itemViewHolder.mTime.setText(recipes.getTotalTime() + "min");
        itemViewHolder.mDescription.setText(recipes.getInstructions());
        itemViewHolder.big_card.setTag(recipes.getId());
//                    itemViewHolder.bigR_likebutton.setTag(recipes.getId());
        Picasso.get().load(recipes.getImgUrl()).into(itemViewHolder.imageView);


        String ingredientsList = recipes.getIngredientsList();
        String cleanedIngredients = recipes.getCleanedIngredients();
        String instructions = recipes.getInstructions();
        String s = ingredientsList + cleanedIngredients ;
        String[] card1 = {"chicken", "egg", "mutton", "fish", "shrimp", "prawns", "pomphret",
                "surmai", "beef", "goat"};

        if(instructions.length()<750){//easy
            itemViewHolder.bigR_difficulty.setText("Easy");

        }else if(instructions.length()>1400){//hard
            itemViewHolder.bigR_difficulty.setText("Hard");
        }else{
            itemViewHolder.bigR_difficulty.setText("Medium");
        }


        if (IsNonveg(s.toLowerCase(), card1)) {
            itemViewHolder.bigR_nonveg_icon.setVisibility(View.VISIBLE);
            itemViewHolder.bigR_veg_icon.setVisibility(View.GONE);
        } else {
            itemViewHolder.bigR_veg_icon.setVisibility(View.VISIBLE);
            itemViewHolder.bigR_nonveg_icon.setVisibility(View.GONE);
        }


    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


                View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.recipe_card_design, viewGroup, false);

        return new ItemViewHolder((layoutView));

    }

}
