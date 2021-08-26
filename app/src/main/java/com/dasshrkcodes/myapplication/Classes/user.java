package com.dasshrkcodes.myapplication.Classes;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class user implements Serializable{

    private final String username;
    private final String email;
    private final List<String> cuisines;
    private final List<String> history;
    private final List<String> ownRecipe;
    private final List<String> likedRecipe;
    private final Map<String,String> userRatings;
    private final String language;

    public user(String username, String email, List<String> cuisines, List<String> history, List<String> ownRecipe,
                List<String> likedRecipe, String language,Map<String,String> userRatings) {
        this.username = username;
        this.email = email;
        this.cuisines = cuisines;
        this.history = history;
        this.ownRecipe = ownRecipe;
        this.likedRecipe = likedRecipe;
        this.language = language;
        this.userRatings = userRatings;
    }

    public String getUsername(){ return username;}
    public String getEmail(){ return email;}
    public List<String> getCuisines(){ return cuisines;}
    public List<String> getHistory(){ return history;}
    public List<String> getOwnRecipe(){ return ownRecipe;}
    public List<String> getLikedRecipe(){ return likedRecipe;}
    public String getLanguage(){ return language;}
    public Map<String,String> getuserRatings(){ return userRatings;}

}
