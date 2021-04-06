package com.example.shareeat;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shareeat.model.Model;
import com.example.shareeat.model.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;

public class RecipeDetails extends Fragment {

    FirebaseUser user;
    String recipeId;
    Recipe rcp;
    Recipe recDel;
    TextView recipeTitle;
    TextView nickname;
    TextView category;
    TextView detailRecipe;
    ImageView pictureRecipe;
    ImageView edit_btn;
    ImageView deleteRecipe;
    ImageView closeWindow;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_recipe_details, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();

        nickname = view.findViewById(R.id.details_nickname);
        recipeTitle = view.findViewById(R.id.details_recipeTitle);
        category = view.findViewById(R.id.details_category);
        detailRecipe = view.findViewById(R.id.deatils_detailRecipe);
        closeWindow = view.findViewById(R.id.details_closeImg);
        pictureRecipe = view.findViewById(R.id.details_image);
        editRecipe = view.findViewById(R.id.details_editImg);
        deleteRecipe = view.findViewById(R.id.details_deleteImg);
        recipeId = RecipeDetailsArgs.fromBundle(getArguments()).getRecipeId();
        edit_btn= view.findViewById(R.id.details_editImg);
        deleteRecipe= view.findViewById(R.id.details_deleteImg);

        edit_btn.setVisibility(View.INVISIBLE);
        deleteRecipe.setVisibility(View.INVISIBLE);


        Model.instance.getRecipe(recipeId, new Model.GetRecipeListener() {
            @Override
            public void onComplete(Recipe recipe) {

                rcp = recipe;
                nickname.setText(rcp.getUserName());
                recipeTitle.setText(rcp.getTitleRecipe());
                category.setText(rcp.getCategory());
                detailRecipe.setText(rcp.getRecipe());
                pictureRecipe.setImageResource(R.drawable.recipe_placeholder);
                if (recipe.getImageUrl() != null) {
                    Picasso.get().load(recipe.getImageUrl()).placeholder(R.drawable.recipe_placeholder).into(pictureRecipe);
                }
                if(rcp.getUserId().equals(user.getUid())){
                    edit_btn.setVisibility(View.VISIBLE);
                    deleteRecipe.setVisibility(View.VISIBLE);
                }
            }
        });




        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeDetailsDirections.ActionRecipeDetailsToEditRecipe direction = RecipeDetailsDirections.actionRecipeDetailsToEditRecipe(recipeId);
                Navigation.findNavController(getActivity(), R.id.mainactivity_navhost).navigate(direction);
                Log.d("TAG", "Recipe Id i sent : " + recipeId);
            }
        });
        closeWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });

//        if (recipeId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            deleteRecipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Model.instance.getRecipe(recipeId, new Model.GetRecipeListener() {
                        @Override
                        public void onComplete(Recipe recipe) {
                            recDel = recipe;
                            Model.instance.deleteRecipe(recDel, new Model.DeleteRecipeListener() {
                                @Override
                                public void onComplete() {
                                    Navigation.findNavController(view).popBackStack();
                                }
                            });
                        }
                    });

                }
            });
//        }
//        else
//            deleteRecipe.setEnabled(false);
        
        return view;
    }
}