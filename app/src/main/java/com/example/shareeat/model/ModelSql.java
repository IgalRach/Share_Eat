package com.example.shareeat.model;

import android.os.AsyncTask;

import java.util.List;

public class ModelSql {
    public void getAllRecipes(Model.GetAllRecipesListener listener) {
        class MyAsyncTask extends AsyncTask {
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
