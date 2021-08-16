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

import com.google.android.material.card.MaterialCardView;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE = 1;
    private final Context context;
    private final List<Recipes> listRecyclerItem;


    public RecyclerAdapter(Context context, List<Recipes> listRecyclerItem) {
        this.context = context;
        this.listRecyclerItem = listRecyclerItem;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        LikeButton bigR_likebutton;
        TextView mTitle, mDescription, mTime;
        MaterialCardView big_card;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.bigR_featured_image);
            mTitle = itemView.findViewById(R.id.bigR_featured_title);
            mDescription = itemView.findViewById(R.id.bigR_featured_desc);
            mTime = itemView.findViewById(R.id.bigR_cooktime);
            big_card = itemView.findViewById(R.id.big_card);
            bigR_likebutton = itemView.findViewById(R.id.bigR_likebutton);

        }
    }


    public void saveObjectToSharedPreference(Context context, String preferenceFileName,
                                             String serializedObjectKey, List<Recipes> object) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(object);
        sharedPreferencesEditor.putString(serializedObjectKey, serializedObject);
        sharedPreferencesEditor.apply();
    }

    public List<Recipes> getSavedObjectFromPreference(Context context, String preferenceFileName
            , String preferenceKey, List<Recipes> classType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        if (sharedPreferences.contains(preferenceKey)) {
            final Gson gson = new Gson();
            Type collectionType = new TypeToken<List<Recipes>>(){}.getType();
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""),collectionType);
        }
        return null;
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



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        int viewType = getItemViewType(i);

        switch (viewType) {
            case TYPE:
                default:

                    ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
                    Recipes recipes = (Recipes) listRecyclerItem.get(i);

                    itemViewHolder.mTitle.setText(recipes.getRName());
                    itemViewHolder.mTime.setText(recipes.getTotalTime()+"min");
                    itemViewHolder.mDescription.setText(recipes.getInstructions());
                    itemViewHolder.big_card.setTag(recipes.getId());
                    itemViewHolder.bigR_likebutton.setTag(recipes.getId());
                    Picasso.get().load(recipes.getImgUrl()).into(itemViewHolder.imageView);

//                    LikedRecipeList =getSavedObjectFromPreference(context,"LikedRecipeList",
//                            "LikedRecipeList",listRecyclerItem);

                    itemViewHolder.bigR_likebutton.setOnLikeListener(new OnLikeListener() {
                        @Override
                        public void liked(LikeButton likeButton) {
                            Toast.makeText(context, "liked"+i, Toast.LENGTH_SHORT).show();

                            List<Recipes> LikedRecipeList=getSavedObjectFromPreference(context,"LikedRecipeList",
                                    "LikedRecipeList",listRecyclerItem);
                            LikedRecipeList.add(listRecyclerItem.get(i));

                            saveObjectToSharedPreference(context,"LikedRecipeList",
                                    "LikedRecipeList",LikedRecipeList);

                        }
                        @Override
                        public void unLiked(LikeButton likeButton) {
                             List<Recipes>LikedRecipeList=getSavedObjectFromPreference(context,"LikedRecipeList",
                                    "LikedRecipeList",listRecyclerItem);
//                            Toast.makeText(context, "disliked"+LikedRecipeList.indexOf(listRecyclerItem.get(i)), Toast.LENGTH_SHORT).show();

                            LikedRecipeList.remove(LikedRecipeList.indexOf(listRecyclerItem.get(i)));
                            saveObjectToSharedPreference(context,"LikedRecipeList",
                                    "LikedRecipeList",LikedRecipeList);
                        }
                    });

        }

    }

    @Override
    public int getItemCount() {
        return listRecyclerItem.size();
    }
}
