package com.dasshrkcodes.myapplication;

import java.io.Serializable;

public class user implements Serializable{

    private final String username;
    private final String email;
    private final String[] cuisines;
    private final String[] searches;
    private final String[] history;
    private final String[] ownRecipe;
    private final String[] likedRecipe;
    private final String language;

    public user(String username, String email, String[] cuisines, String[] searches, String[] history, String[] ownRecipe,
                String[] likedRecipe, String language) {
        this.username = username;
        this.email = email;
        this.cuisines = cuisines;
        this.searches = searches;
        this.history = history;
        this.ownRecipe = ownRecipe;
        this.likedRecipe = likedRecipe;
        this.language = language;
    }

    public String getUsername(){ return username;}
    public String getEmail(){ return email;}
    public String[] getCuisines(){ return cuisines;}
    public String[] getSearches(){ return searches;}
    public String[] getHistory(){ return history;}
    public String[] getOwnRecipe(){ return ownRecipe;}
    public String[] getLikedRecipe(){ return likedRecipe;}
    public String getLanguage(){ return language;}

}
