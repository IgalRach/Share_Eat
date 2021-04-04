package com.example.shareeat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.shareeat.model.Model;
import com.example.shareeat.model.Recipe;
import com.squareup.picasso.Picasso;


public class EditRecipe extends AddPost {

    String recipeId;
    TextView addPost_Title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = super.onCreateView(inflater, container, savedInstanceState);
        addPost_Title= view.findViewById(R.id.addPost_Title);
        recipeId = EditRecipeArgs.fromBundle(getArguments()).getRecipeId();
        Log.d("TAG", "recipe id i got: " + recipeId);
        addPost_Title.setText("Edit Post");

        Model.instance.getRecipe(recipeId, new Model.GetRecipeListener() {
            @Override
            public void onComplete(Recipe recipe) {
                recipeNameEditText.setText(recipe.getTitleRecipe());
                //Spinner.setText(recipe.getCategory());
                recipeEditText.setText(recipe.getRecipe());
                avatarImageView.setImageResource(R.drawable.recipe_placeholder);
                if(recipe.getImageUrl()!=null){
                    Picasso.get().load(recipe.getImageUrl()).placeholder(R.drawable.recipe_placeholder).into(avatarImageView);
                }
            }
        });




        return view;
    }
}