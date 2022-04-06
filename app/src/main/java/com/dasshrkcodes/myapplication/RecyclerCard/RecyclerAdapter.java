package com.dasshrkcodes.myapplication.RecyclerCard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dasshrkcodes.myapplication.R;
import com.dasshrkcodes.myapplication.Classes.Recipes;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.card.MaterialCardView;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE = 1;
    private final Context context;
    private final List<Recipes> listRecyclerItem;
    private Liked_click_RecyclerView liked_mCallback;
    private UnLiked_click_RecyclerView unliked_mCallback;
    private OnItem_click_RecyclerView onItemclick_mCallback;


    public RecyclerAdapter(Context context, List<Recipes> listRecyclerItem,
                           Liked_click_RecyclerView liked_mCallback,
                           UnLiked_click_RecyclerView unliked_mCallback,
                           OnItem_click_RecyclerView onItemclick_mCallback) {
        this.context = context;
        this.listRecyclerItem = listRecyclerItem;
        this.liked_mCallback = liked_mCallback;
        this.unliked_mCallback = unliked_mCallback;
        this.onItemclick_mCallback = onItemclick_mCallback;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView, bigR_veg_icon, bigR_nonveg_icon;
        LikeButton bigR_likebutton;
        TextView mTitle, mDescription, mTime, bigR_difficulty, bigR_ratingcount;
        MaterialCardView big_card;
        RatingBar bigR_ratingbar;

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
            bigR_ratingbar = itemView.findViewById(R.id.bigR_ratingbar);
            bigR_ratingcount = itemView.findViewById(R.id.bigR_ratingcount);

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

    public List<String> getLikedRecipeList(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<List<String>>() {
        }.getType();
        return gson.fromJson(json, type);
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
                itemViewHolder.bigR_ratingcount.setText(recipes.getRatingCount() + "");
                itemViewHolder.big_card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemclick_mCallback.ItemeOnClick(recipes);
                    }
                });
                itemViewHolder.bigR_ratingbar.setRating(Float.parseFloat(recipes.getRating()));

//                    itemViewHolder.bigR_likebutton.setTag(recipes.getId());
                Picasso.get().load(recipes.getImgUrl()).into(itemViewHolder.imageView);
//                Picasso.get().load(recipes.getImgUrl()).resize(300, 200).into(itemViewHolder.imageView);


                String ingredientsList = recipes.getIngredientsList();
                String cleanedIngredients = recipes.getCleanedIngredients();
                String instructions = recipes.getInstructions();
                String s = ingredientsList + cleanedIngredients;
                String[] card1 = {"chicken", "egg", "mutton", "fish", "shrimp", "prawns", "pomphret",
                        "surmai", "beef", "goat"};

                if (instructions.length() < 750) {//easy
                    itemViewHolder.bigR_difficulty.setText("Easy");

                } else if (instructions.length() > 1400) {//hard
                    itemViewHolder.bigR_difficulty.setText("Hard");
                } else {
                    itemViewHolder.bigR_difficulty.setText("Medium");
                }


                if (IsNonveg(s.toLowerCase(), card1)) {
                    itemViewHolder.bigR_nonveg_icon.setVisibility(View.VISIBLE);
                    itemViewHolder.bigR_veg_icon.setVisibility(View.GONE);
                } else {
                    itemViewHolder.bigR_veg_icon.setVisibility(View.VISIBLE);
                    itemViewHolder.bigR_nonveg_icon.setVisibility(View.GONE);
                }


                GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(context);
                if (signInAccount != null) {//user available

                    List<String> likedRecipe_test = getLikedRecipeList("likedRecipeListIds");
                    if (likedRecipe_test.contains(recipes.getId())) {
                        itemViewHolder.bigR_likebutton.setLiked(true);
                    } else {
                        itemViewHolder.bigR_likebutton.setLiked(false);
                    }
                }



                 Vibrator myVib = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
                itemViewHolder.bigR_likebutton.setOnLikeListener(new OnLikeListener() {
                    @Override
                    public void liked(LikeButton likeButton) {
//                            Toast.makeText(context, "liked"+i, Toast.LENGTH_SHORT).show();
                        liked_mCallback.LikeonClick(listRecyclerItem.get(i));
                        myVib.vibrate(50);
                    }

                    @Override
                    public void unLiked(LikeButton likeButton) {
                        unliked_mCallback.UnLikeonClick(listRecyclerItem.get(i));
                        myVib.vibrate(100);
                    }
                });

        }

    }

    @Override
    public int getItemCount() {
        return listRecyclerItem.size();
    }
}
