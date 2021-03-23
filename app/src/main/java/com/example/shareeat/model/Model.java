package com.example.shareeat.model;


import android.app.Activity;
import java.util.LinkedList;
import java.util.List;
import android.os.*;

public class Model {
     public static final Model instance = new Model();
   // List<Recipe> data = new LinkedList<Recipe>();

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
//        for(int i=0; i<100; i++) {
//            Recipe recipe = new Recipe();
//            recipe.setTitleRecipe("1234"+i);
//            recipe.setRecipe("five lemons , 20 apples"+i);
//            data.add(recipe);
//        }
    }

    public interface GetAllRecipesListener{
        void onComplete(List<Recipe> data);
    }

    public void getAllRecipes(GetAllRecipesListener listener){
        class MyAsyncTask extends AsyncTask{
            List<Recipe> data;
            @Override
            protected Object doInBackground(Object[] objects) {
                data= AppLocalDb.db.recipeDao().getAllRecipes();
                return null;
            }
            @Override
            protected void onPostExecute(Object o){
                super.onPostExecute(o);
                listener.onComplete(data);
            }

        }
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
   //  List<Recipe> data= AppLocalDb.db.recipeDao().getAllRecipes();

//        return data;
    }

    public interface AddRecipeListener{
        void onComplete();
    }

    public void addRecipe(final Recipe recipe,AddRecipeListener listener) {
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDb.db.recipeDao().insertAll(recipe);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null){
                    listener.onComplete();
                }
            }
        };
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }
}

