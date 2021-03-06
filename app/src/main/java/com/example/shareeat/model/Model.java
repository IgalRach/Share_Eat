package com.example.shareeat.model;

import java.util.List;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import com.example.shareeat.MyApplication;

public class Model {
     public static final Model instance = new Model();
     ModelFirebase modelFirebase = new ModelFirebase();
    LiveData<List<Recipe>> recipeList;

    private Model(){
    }
    public interface Listener<T>{
        void onComplete(T result);
    }
    public interface GetAllRecipesListener{
        void onComplete();
    }

    public LiveData<List<Recipe>> getAllRecipes(){
        recipeList = AppLocalDb.db.recipeDao().getAllRecipes();
        refreshAllRecipes(null);
        return recipeList;
    }

    public LiveData<List<Recipe>> getRecipesByCategory(String category) {
        recipeList = AppLocalDb.db.recipeDao().getRecipesByCategory(category);
        refreshAllRecipes(null);
        return recipeList;
    }

    public LiveData<List<Recipe>> getAllRecipesPerUser(String userId) {
        recipeList = AppLocalDb.db.recipeDao().getUserRecipes(userId);
        refreshAllRecipes(null);
        return recipeList;
    }

    public void refreshAllRecipes(final GetAllRecipesListener listener){
        //get local last update date
        final SharedPreferences sp = MyApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
        long lastUpdated = sp.getLong("lastUpdated", 0);

        //get all updated record from firebase from the last updated
        modelFirebase.getAllRecipes(lastUpdated, new ModelFirebase.GetAllRecipesListener() {

            @SuppressLint("StaticFieldLeak")
            @Override
            public void onComplete(final List<Recipe> data) {
                new AsyncTask<String,String,String>(){
                    @Override
                    protected String doInBackground(String... strings) {
                        long lastUpdated = 0;
                        for(Recipe recipe : data){
                            AppLocalDb.db.recipeDao().insertAll(recipe);
                            if (recipe.getUpdatedDate() > lastUpdated) lastUpdated = recipe.getUpdatedDate();
                        }
                        SharedPreferences.Editor edit = MyApplication.context.getSharedPreferences("TAG",Context.MODE_PRIVATE).edit();
                        edit.putLong("RecipesLastUpdateDate",lastUpdated);
                        edit.commit();
                        return "";
                    }
                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        //cleanLocalDb();
                        if (listener!=null)
                            listener.onComplete();
                    }
                }.execute("");
            }
        });
    }

    //Get
    public interface GetRecipeListener{
        void onComplete(Recipe recipe);
    }
    public void getRecipe(String id, GetRecipeListener listener){
        modelFirebase.getRecipe(id, listener);
    }

    //Add
    public interface AddRecipeListener{
        void onComplete();
    }
    public void addRecipe(final Recipe recipe,final AddRecipeListener listener) {
        modelFirebase.addRecipe(recipe, listener);
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                AppLocalDb.db.recipeDao().insertAll(recipe);
                return "";
            }
        }.execute();
    }

    //Update
    public void updateRecipe(final Recipe recipe,final AddRecipeListener listener) {
        modelFirebase.addRecipe(recipe, listener);
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                AppLocalDb.db.recipeDao().insertAll(recipe);
                return "";
            }
        }.execute();
    }

    //Delete
    public interface DeleteRecipeListener extends AddRecipeListener {}
    public void deleteRecipe(Recipe recipe, DeleteRecipeListener listener){
        modelFirebase.deleteRecipe(recipe);
        modelFirebase.delete(recipe, listener);
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                AppLocalDb.db.recipeDao().deleteRecipe(recipe);
                return "";
            }
        }.execute();
    }

    //Upload
    public interface UploadImageListener extends Listener<String> {}
    public void uploadImage(Bitmap imageBmp, String name, final UploadImageListener listener){
        modelFirebase.uploadImage(imageBmp,name,listener);
    }

    public void updateUserProfile(User user) {
        ModelFirebase.updateUserProfile(user);
    }

}


