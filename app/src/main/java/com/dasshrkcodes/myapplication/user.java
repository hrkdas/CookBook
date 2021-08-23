package com.dasshrkcodes.myapplication;

import java.io.Serializable;
import java.util.List;

public class user implements Serializable{

    private final String username;
    private final String email;
    private final List<String> cuisines;
    private final List<String> searches;
    private final List<String> history;
    private final List<String> ownRecipe;
    private final List<String> likedRecipe;
    private final String language;

    public user(String username, String email, List<String> cuisines, List<String> searches, List<String> history, List<String> ownRecipe,
                List<String> likedRecipe, String language) {
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
    public List<String> getCuisines(){ return cuisines;}
    public List<String> getSearches(){ return searches;}
    public List<String> getHistory(){ return history;}
    public List<String> getOwnRecipe(){ return ownRecipe;}
    public List<String> getLikedRecipe(){ return likedRecipe;}
    public String getLanguage(){ return language;}

}
