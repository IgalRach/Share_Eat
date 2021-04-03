package com.example.shareeat;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shareeat.model.Model;
import com.example.shareeat.model.Recipe;

import java.io.File;

public class RecipeDetails extends Fragment {

    String recipeId;
    Recipe rcp;
    TextView title;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_recipe_details, container, false);
//        title = view.findViewById(R.id.addPost_Title);
//        title.setText("Recipe Details");

//        recipeNameEditText.setEnabled(false);
//        recipeEditText.setEnabled(false);
//        spinner.setEnabled(false);
//
//        recipeNameEditText.setBackgroundColor(Color.parseColor("#D2000000"));
//        recipeEditText.setBackgroundColor(Color.parseColor("#D2000000"));
//        addBtn.setVisibility(View.INVISIBLE);
//        Imageurl.setVisibility(View.INVISIBLE);
        recipeId = RecipeDetailsArgs.fromBundle(getArguments()).getRecipeId();

        Model.instance.getRecipe(recipeId, new Model.GetRecipeListener() {
            @Override
            public void onComplete(Recipe recipe) {
                rcp = recipe;
//                recipeNameEditText.setText(rcp.getTitleRecipe());
//                recipeEditText.setText(rcp.getRecipe());
            }
        });

        return view;
    }
}