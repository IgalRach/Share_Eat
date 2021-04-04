package com.example.shareeat;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.UUID;


public class EditRecipe extends AddPost {

    String recipeId;
    TextView addPost_Title;
    Recipe rcp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = super.onCreateView(inflater, container, savedInstanceState);
        addPost_Title= view.findViewById(R.id.addPost_Title);
        recipeId = EditRecipeArgs.fromBundle(getArguments()).getRecipeId();
        Log.d("TAG", "recipe id i got: " + recipeId);
        addPost_Title.setText("Edit Post");
        addBtn.setText("Update");
        Model.instance.getRecipe(recipeId, new Model.GetRecipeListener() {
            @Override
            public void onComplete(Recipe recipe) {
                rcp=recipe;
                recipeNameEditText.setText(recipe.getTitleRecipe());
                recipeEditText.setText(recipe.getRecipe());
                avatarImageView.setImageResource(R.drawable.recipe_placeholder);
                if(recipe.getImageUrl()!=null){
                    Picasso.get().load(recipe.getImageUrl()).placeholder(R.drawable.recipe_placeholder).into(avatarImageView);
                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG","Image "+rcp.getImageUrl());
                  UpdateRecipe(v);
            }
        });


        return view;
    }

    private void UpdateRecipe(View view){
        if (recipeNameEditText.getText().length() == 0 || recipeEditText.getText().length() == 0 || category.length() == 0) {
            Snackbar.make(view, "You must fill all the fields", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Log.d("TAG", "Some of the fields are empty.");
        }
        else{
            rcp.setTitleRecipe(recipeNameEditText.getText().toString());
            rcp.setCategory(spinner.getSelectedItem().toString());
            rcp.setRecipe(recipeEditText.getText().toString());
            rcp.getImageUrl();

            addBtn.setEnabled(false);
            cancelBtn.setEnabled((false));
            editImage.setEnabled(false);

            BitmapDrawable drawable = (BitmapDrawable) avatarImageView.getDrawable();
            Bitmap bitmap=drawable.getBitmap();

            Model.instance.uploadImage(drawable.getBitmap(), rcp.getId(), new Model.UploadImageListener() {
                @Override
                public void onComplete(String url) {
                    if(url==null){
                        displayFailedError();
                    }
                    else{
                        rcp.setImageUrl(url);
                        pb.setVisibility(View.VISIBLE);
                        Model.instance.updateRecipe(rcp, new Model.AddRecipeListener() {
                            @Override
                            public void onComplete() {
                                pb.setVisibility(View.INVISIBLE);
                                addBtn.setEnabled(true);
                                cancelBtn.setEnabled((false));
                                editImage.setEnabled(false);
                                Navigation.findNavController(addBtn).popBackStack();
                            }
                        });
                    }
                }
            });
        }

    }

}