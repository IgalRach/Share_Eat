package com.example.shareeat;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.shareeat.model.Model;
import com.example.shareeat.model.Recipe;

import java.util.List;

public class MyRecipeViewModel extends ViewModel {

    private LiveData<List<Recipe>> recipeLiveData;

    public MyRecipeViewModel() {
        Log.d("TAG", "********* MyRecipeViewModel");
        recipeLiveData = Model.instance.getAllRecipes();
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
