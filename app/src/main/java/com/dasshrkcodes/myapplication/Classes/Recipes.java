package com.dasshrkcodes.myapplication.Classes;

import java.io.Serializable;

public class Recipes implements Serializable {
    private final String name;
    private final String ingredientsList;
    private final String totalTime;
    private final String cuisine;
    private final String instructions;
    private final String cleanedIngredients;
    private final String imgUrl;
    private final String ingredientCount;
    private final String rating;
    private final String ratingCount;
    private final String id;

    public Recipes(String name, String ingredientsList, String totalTime, String cuisine, String instructions, String cleanedIngredients,
                   String imgUrl, String ingredientCount, String rating, String ratingCount,String id) {
        this.name = name;
        this.ingredientsList = ingredientsList;
        this.totalTime = totalTime;
        this.cuisine = cuisine;
        this.instructions = instructions;
        this.cleanedIngredients = cleanedIngredients;
        this.imgUrl = imgUrl;
        this.ingredientCount = ingredientCount;
        this.rating = rating;
        this.ratingCount = ratingCount;
        this.id = id;
    }

    public String getRName(){ return name;}
    public String getIngredientsList(){ return ingredientsList;}
    public String getTotalTime(){ return totalTime;}
    public String getCuisine(){ return cuisine;}
    public String getInstructions(){ return instructions;}
    public String getCleanedIngredients(){ return cleanedIngredients;}
    public String getImgUrl(){ return imgUrl;}
    public String getIngredientCount(){ return ingredientCount;}
    public String getRating(){ return rating;}
    public String getRatingCount(){ return ratingCount;}
    public String getId(){ return id;}
}


