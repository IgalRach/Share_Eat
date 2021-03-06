package com.example.shareeat;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.shareeat.model.Model;
import com.example.shareeat.model.Recipe;

import java.util.List;

public class RecipeViewModel extends ViewModel {

    private LiveData<List<Recipe>> recipeLiveData;

    public RecipeViewModel() {
        Log.d("TAG", "RecipeViewModel");
        recipeLiveData = Model.instance.getAllRecipes();
    }

    public LiveData<List<Recipe>> getRecipesByCategory(String categoryId){
            recipeLiveData = Model.instance.getRecipesByCategory(categoryId);
        return recipeLiveData;
    }

    public LiveData<List<Recipe>> getDataByUser(String userId){
        LiveData<List<Recipe>> myRecipeLiveData;
        myRecipeLiveData = Model.instance.getAllRecipesPerUser(userId);
        return myRecipeLiveData;
    }

    public LiveData<List<Recipe>> getData() {
        return recipeLiveData;
    }

}
