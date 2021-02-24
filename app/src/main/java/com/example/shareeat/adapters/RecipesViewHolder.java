package com.example.shareeat.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareeat.R;
import com.example.shareeat.model.Recipe;

import de.hdodenhof.circleimageview.CircleImageView;


public class RecipesViewHolder extends RecyclerView.ViewHolder{

    public RecipesAdapter.OnItemClickListener listener;
    CircleImageView profilePic;
    TextView nickname;
    TextView recipeTitle;
    TextView recipe;
    ImageView postImg;
    int position;

    public RecipesViewHolder(@NonNull View itemView) {
        super(itemView);
        profilePic=itemView.findViewById(R.id.profile_profile_im);
        nickname = itemView.findViewById(R.id.listRow_nickname);
        recipeTitle=itemView.findViewById(R.id.listRow_titleRec);
        recipe= itemView.findViewById(R.id.listRow_recipe);
        postImg=itemView.findViewById(R.id.listRow_img);

        itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                listener.OnItemClick(position);
            }
        });
    }

    public void bindData(Recipe recipe, int position) {
        recipeTitle.setText(recipe.getTitleRecipe());
        this.position= position;
    }
}
