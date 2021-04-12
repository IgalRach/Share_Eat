package com.example.shareeat.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shareeat.R;
import com.example.shareeat.model.Recipe;
import com.squareup.picasso.Picasso;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;


public class RecipesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final private OnItemClickListener listener;
    Context context;
    List<Recipe> recipeArrayList;

    public RecipesAdapter(Context context, List<Recipe> recipeArrayList, OnItemClickListener onClickListener) {
        this.context = context;
        this.recipeArrayList = recipeArrayList;
        this.listener = onClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.list_row, parent, false);
        return new RecipesViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Recipe recipe = recipeArrayList.get(position);
        RecipesViewHolder viewHolder = (RecipesViewHolder) holder;
       // getImageFromFireBase(recipe);
        viewHolder.profilePic.setImageResource(R.drawable.ic_round_person_grey);
        viewHolder.nickname.setText(recipe.getUserName());
        viewHolder.recipeTitle.setText(recipe.getTitleRecipe());
        viewHolder.category.setText(recipe.getCategory());
        viewHolder.postImg.setImageResource(R.drawable.icon_upload_image);
        if (recipe.getImageUrl() != null) {
            Picasso.get().load(recipe.getImageUrl()).placeholder(R.drawable.recipe_placeholder).into(viewHolder.postImg);
        }

        if( recipe.getUserPic()!=null){
            Picasso.get().load(recipe.getUserPic()).placeholder(R.drawable.ic_round_person_grey).into(viewHolder.profilePic);
        }
    }


    @Override
    public int getItemCount() {
        return recipeArrayList.size();
    }

    class RecipesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView profilePic;
        TextView nickname;
        TextView recipeTitle;
        TextView category;
        ImageView postImg;

        public RecipesViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            profilePic=itemView.findViewById(R.id.detailsprofile_profile_im);
            nickname = itemView.findViewById(R.id.listRow_nickname);
            recipeTitle=itemView.findViewById(R.id.listRow_titleRec);
            category= itemView.findViewById(R.id.listRow_category);
            postImg=itemView.findViewById(R.id.listRow_img);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            listener.onItemClick(position);
        }
    }
}

