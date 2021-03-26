package com.example.shareeat.model;


import android.app.Activity;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.*;
import com.example.shareeat.model.ModelFirebase;

public class Model {
    ModelFirebase modelFirebase;
    public static final Model instance = new Model();

    public interface Listener<T> {
        void onComplete(T t);
    }

    public interface CompListener {
        void onComplete();
    }

    public void setUserAppData(String email) {
        ModelFirebase.setUserAppData(email);
    }

    private Model() {

    }

    public interface GetAllRecipesListener {
        void onComplete(List<Recipe> data);
    }

    public void getAllRecipes(GetAllRecipesListener listener) {
        class MyAsyncTask extends AsyncTask {
            List<Recipe> data;

            @Override
            protected Object doInBackground(Object[] objects) {
                data = AppLocalDb.db.recipeDao().getAllRecipes();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                listener.onComplete(data);
            }

        }
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }

    public interface AddRecipeListener {
        void onComplete();
    }

    public void addRecipe(final Recipe recipe, AddRecipeListener listener) {
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDb.db.recipeDao().insertAll(recipe);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null) {
                    listener.onComplete();
                }
            }
        };
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }
}

