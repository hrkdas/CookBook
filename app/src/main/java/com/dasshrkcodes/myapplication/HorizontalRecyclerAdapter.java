package com.dasshrkcodes.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
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



        ImageView imageView,smallR_nonveg_icon,smallR_veg_icon;
        TextView mTitle, mDescription,smallR_ratingcount;
        MaterialCardView small_card;
        RatingBar smallR_ratingbar;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.smallR_featured_image);
            mTitle = itemView.findViewById(R.id.smallR_featured_title);
            mDescription = itemView.findViewById(R.id.smallR_featured_desc);
            small_card = itemView.findViewById(R.id.small_card);

            smallR_nonveg_icon = itemView.findViewById(R.id.smallR_nonveg_icon);
            smallR_veg_icon = itemView.findViewById(R.id.smallR_veg_icon);
            smallR_ratingbar = itemView.findViewById(R.id.smallR_ratingbar);
            smallR_ratingcount = itemView.findViewById(R.id.smallR_ratingcount);


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

                    Picasso.get().load(recipes.getImgUrl()).resize(420,200).into(itemViewHolder.imageView);

                    itemViewHolder.smallR_ratingbar.setRating(Float.parseFloat(recipes.getRating()));

                    String ingredientsList = recipes.getIngredientsList();
                    String cleanedIngredients = recipes.getCleanedIngredients();
                    String s = ingredientsList + cleanedIngredients ;
                    String[] card1 = {"chicken", "egg", "mutton", "fish", "shrimp", "prawns", "pomphret",
                            "surmai", "beef", "goat"};


                    if (IsNonveg(s.toLowerCase(), card1)) {
                        itemViewHolder.smallR_nonveg_icon.setVisibility(View.VISIBLE);
                        itemViewHolder.smallR_veg_icon.setVisibility(View.GONE);
                    } else {
                        itemViewHolder.smallR_veg_icon.setVisibility(View.VISIBLE);
                        itemViewHolder.smallR_nonveg_icon.setVisibility(View.GONE);
                    }



        }

    }

    @Override
    public int getItemCount() {
        return listRecyclerItem.size();
    }
}
