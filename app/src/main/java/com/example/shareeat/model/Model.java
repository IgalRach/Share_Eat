package com.example.shareeat.model;


import android.app.Activity;
import java.util.LinkedList;
import java.util.List;
import android.os.*;

public class Model {
     public static final Model instance = new Model();
     ModelFirebase modelFirebase = new ModelFirebase();
     ModelSql modelSql = new ModelSql();

    private Model(){
    }

    public interface Listener<T>{
        void onComplete(T result);
    }

    public interface GetAllRecipesListener extends Listener<List<Recipe>>{
        void onComplete(List<Recipe> data);
    }
    public void getAllRecipes(GetAllRecipesListener listener){
        modelFirebase.getAllRecipes(listener);
        //AppLocalDb.db.recipeDao().getAllRecipes();
        //modelSql.getAllRecipes(listener);
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
    public void addRecipe(final Recipe recipe,AddRecipeListener listener) {
        modelFirebase.addRecipe(recipe, listener);
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

