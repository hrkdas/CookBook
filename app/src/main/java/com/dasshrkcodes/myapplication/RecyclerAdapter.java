package com.dasshrkcodes.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE = 1;
    private final Context context;
    private final List<Recipes> listRecyclerItem;
    private List<Recipes> wishlistRecipeList;
    private Liked_click_RecyclerView mCallback;


    public RecyclerAdapter(Context context, List<Recipes> listRecyclerItem, Liked_click_RecyclerView mCallback, List<Recipes> wishlistRecipeList) {
        this.context = context;
        this.listRecyclerItem = listRecyclerItem;
        this.mCallback = mCallback;
        this.wishlistRecipeList = wishlistRecipeList;
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


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case TYPE:
            default:

                View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.recipe_card_design, viewGroup, false);

                return new ItemViewHolder((layoutView));
        }

    }

    public List<Recipes> getSavedObjectFromPreference(Context context, String preferenceFileName
            , String preferenceKey, List<Recipes> classType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        if (sharedPreferences.contains(preferenceKey)) {
            final Gson gson = new Gson();
            Type collectionType = new TypeToken<List<Recipes>>() {
            }.getType();
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), collectionType);
        }
        return null;
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

                wishlistRecipeList = getSavedObjectFromPreference(context, "LikedRecipeList",
                        "LikedRecipeList", wishlistRecipeList);
                if (wishlistRecipeList.contains(listRecyclerItem.get(i))) {
                    itemViewHolder.bigR_likebutton.setLiked(true);
                } else {
                    itemViewHolder.bigR_likebutton.setLiked(false);
                }


                itemViewHolder.bigR_likebutton.setOnLikeListener(new OnLikeListener() {
                    @Override
                    public void liked(LikeButton likeButton) {
//                            Toast.makeText(context, "liked"+i, Toast.LENGTH_SHORT).show();
                        mCallback.onClick(listRecyclerItem.get(i));
                    }

                    @Override
                    public void unLiked(LikeButton likeButton) {
                        mCallback.onClick(listRecyclerItem.get(i));
                    }
                });

        }

    }

    @Override
    public int getItemCount() {
        return listRecyclerItem.size();
    }
}
