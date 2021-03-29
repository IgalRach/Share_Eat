package com.example.shareeat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shareeat.model.Model;
import com.example.shareeat.model.Recipe;

public class RecipeDetails extends AddPost {

    String recipeId;
    Recipe rcp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = super.onCreateView(inflater, container, savedInstanceState);

        addBtn.setVisibility(View.INVISIBLE);
        recipeId = RecipeDetailsArgs.fromBundle(getArguments()).getRecipeId();
        Log.d("TAG", "Found recipe with id: " + recipeId);

        Model.instance.getRecipe(recipeId, new Model.GetRecipeListener() {
            @Override
            public void onComplete(Recipe recipe) {
                rcp = recipe;

                recipeNameEditText.setText(recipe.getTitleRecipe());
            }
        });

        return view;
    }
}