package com.example.shareeat.model;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class ModelSql {

    public LiveData<List<Recipe>> getAllRecipes() {
        return AppLocalDb.db.recipeDao().getAllRecipes();
    }

    public void addRecipe(Recipe recipe, Model.AddRecipeListener listener) {
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

    public void deleteRecipe(Recipe recipe, Model.DeleteRecipeListener listener) {
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDb.db.recipeDao().deleteRecipe(recipe);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener  != null)
                    listener.onComplete();
            }
        };
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }
}
