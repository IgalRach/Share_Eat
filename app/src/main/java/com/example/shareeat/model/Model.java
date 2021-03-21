package com.example.shareeat.model;


import android.app.Activity;
import java.util.LinkedList;
import java.util.List;

public class Model {
     public static final Model instance = new Model();
    List<Recipe> data = new LinkedList<Recipe>();

    public interface Listener<T>{
        void onComplete(T t);
    }

    public interface CompListener{
        void onComplete();
    }

    public void setUserAppData(String email){
        ModelFirebase.setUserAppData(email);
    }

    private Model(){
        for(int i=0; i<100; i++) {
            Recipe recipe = new Recipe();
            recipe.setTitleRecipe("1234"+i);
            recipe.setRecipe("five lemons , 20 apples"+i);
            data.add(recipe);
        }
    }

    public List<Recipe> getAllRecipes(){
        return data;
    }

    public void addRecipe(Recipe recipe){

    }


}

