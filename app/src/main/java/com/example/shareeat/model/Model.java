package com.example.shareeat.model;


import android.app.Activity;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.*;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.shareeat.MyApplication;

public class Model {
     public static final Model instance = new Model();
     ModelFirebase modelFirebase = new ModelFirebase();
     ModelSql modelSql = new ModelSql();

    private Model(){
    }

    public interface Listener<T>{
        void onComplete(T result);
    }

    public interface GetAllRecipesListener{
        void onComplete();
    }

    LiveData<List<Recipe>> recipeList;

    public LiveData<List<Recipe>> getAllRecipes(){
        if (recipeList==null){
            recipeList = modelSql.getAllRecipes();
            refreshAllRecipes(null);
        }
        return recipeList;
    }

    public void refreshAllRecipes(final GetAllRecipesListener listener){
        //get local last update date
        final SharedPreferences sp = MyApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
        long lastUpdated = sp.getLong("lastUpdated", 0);

        //get all updated record from firebase from the last updated
        modelFirebase.getAllRecipes(lastUpdated, new ModelFirebase.GetAllRecipesListener() {
            @Override
            public void onComplete(List<Recipe> data) {
                //insert the new updated to the localdb
                long lastUpdated = 0;
                for (Recipe rcp : data){
                    modelSql.addRecipe(rcp, null);
                    if (rcp.getUpdatedDate()>lastUpdated){
                        lastUpdated = rcp.getUpdatedDate();
                    }
                }
                //update the local last update date
                SharedPreferences.Editor editor = sp.edit();
                editor.putLong("lastUpdated", lastUpdated);
                editor.commit();

                //return the updated data to listeners
                if (listener != null){
                    listener.onComplete();
                }
            }
        });
    }

    public interface GetRecipeListener{
        void onComplete(Recipe recipe);
    }
    public void getRecipe(String id, GetRecipeListener listener){
        modelFirebase.getRecipe(id, listener);
    }


    public interface AddRecipeListener{
        void onComplete();
    }
    public void addRecipe(final Recipe recipe,final AddRecipeListener listener) {
        modelFirebase.addRecipe(recipe, new AddRecipeListener() {
            @Override
            public void onComplete() {
                refreshAllRecipes(new GetAllRecipesListener() {
                    @Override
                    public void onComplete() {
                        listener.onComplete();
                    }
                });
            }
        });
        //AppLocalDb.db.recipeDao().insertAll(recipe);
        //modelSql.addRecipe(recipe, listener);
    }


    public interface UpdateStudentListener extends AddRecipeListener {}
    public void updateStudent(final Recipe recipe,UpdateStudentListener listener) {
        modelFirebase.updateStudent(recipe, listener);
        //modelSql.addRecipe(recipe, listener);
    }


    public interface DeleteRecipeListener extends AddRecipeListener {}
    public void deleteRecipe(Recipe recipe, DeleteRecipeListener listener){
        modelFirebase.delete(recipe, listener);
    }
}

