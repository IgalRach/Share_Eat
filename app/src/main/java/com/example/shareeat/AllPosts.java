package com.example.shareeat;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

//import com.example.shareeat.adapters.RecipesAdapter;
//import com.example.shareeat.adapters.RecipesViewHolder;
import com.example.shareeat.model.Model;
import com.example.shareeat.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;
import java.util.NavigableMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class AllPosts extends Fragment {
    RecipeViewModel viewModel;
    ProgressBar pb;
    Button addBtn;
    RecyclerView list;
    RecipesAdapter adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_all_posts, container, false);
        viewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        pb= view.findViewById(R.id.allpost_progressBar);

        // added button from all post to add post.
        addBtn= view.findViewById(R.id.AllPost_addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Navigation.findNavController(view).navigate(R.id.action_allPosts_to_addPost );
            }
        });

        list = view.findViewById(R.id.main_recycler_v);
        list.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);

        pb.setVisibility(View.INVISIBLE);
        
        adapter = new RecipesAdapter();
        list.setAdapter(adapter);

        adapter.setOnClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String recipe = viewModel.getData().getValue().get(position).getId();
                AllPostsDirections.ActionAllPostsToRecipeDetails direction = AllPostsDirections.actionAllPostsToRecipeDetails(recipe);
                Navigation.findNavController(getActivity(), R.id.mainactivity_navhost).navigate(direction);
                Log.d("TAG", "row was clicked " + viewModel.getData().getValue().get(position).getTitleRecipe());
            }
        });

        viewModel.getData().observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                adapter.notifyDataSetChanged();
            }
        });
        reloadData();
        return view;
    }

    public void reloadData(){
        pb.setVisibility(View.VISIBLE);
        addBtn.setEnabled(false);
        Model.instance.refreshAllRecipes(new Model.GetAllRecipesListener() {
            @Override
            public void onComplete() {
                pb.setVisibility(View.INVISIBLE);
                addBtn.setEnabled(true);
            }
        });
    }

    /////////////////////////////////////////////////////////Class ViewHolder
    class RecipesViewHolder extends RecyclerView.ViewHolder{

        public OnItemClickListener listener;
        CircleImageView profilePic;
        TextView nickname;
        TextView recipeTitle;
        TextView recipe;
        TextView category;
        ImageView postImg;
        int position;

        public RecipesViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic=itemView.findViewById(R.id.profile_profile_im);
            nickname = itemView.findViewById(R.id.listRow_nickname);
            recipeTitle=itemView.findViewById(R.id.listRow_titleRec);
            recipe= itemView.findViewById(R.id.listRow_recipe);
            category= itemView.findViewById(R.id.listRow_category);
            postImg=itemView.findViewById(R.id.listRow_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Tag","position is: "+ position);
                    listener.onItemClick(position);
                }
            });
        }

        public void bindData(Recipe recipe, int position) {
            nickname.setText(recipe.getUserName());
            recipeTitle.setText(recipe.getTitleRecipe());
            this.recipe.setText(recipe.getRecipe());
            category.setText(recipe.getCategory());
            this.position= position;
        }
    }

    interface OnItemClickListener {
        void onItemClick(int position);
    }

    /////////////////////////////////////////////////////////Class Adapter
    class RecipesAdapter extends RecyclerView.Adapter<RecipesViewHolder> {

        private OnItemClickListener listener;

        void setOnClickListener(OnItemClickListener listener){
            this.listener = listener;
        }
        @NonNull
        @Override
        public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.list_row, null);
            RecipesViewHolder holder = new RecipesViewHolder(view);
            holder.listener = listener;
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {
            Recipe recipe = viewModel.getData().getValue().get(position);
            holder.bindData(recipe, position);
            holder.nickname.setText(recipe.getUserName());
            holder.category.setText(recipe.getCategory());
            holder.recipe.setText(recipe.getRecipe());
            holder.recipeTitle.setText(recipe.getTitleRecipe());

            holder.postImg.setImageResource(R.drawable.recipe_placeholder);
            if(recipe.getImageUrl()!=null){
                Picasso.get().load(recipe.getImageUrl()).placeholder(R.drawable.recipe_placeholder).into(holder.postImg);
            }

        }

        @Override
        public int getItemCount() {
            if (viewModel.getData().getValue() == null){
                return 0; }
            return viewModel.getData().getValue().size();
        }


    }

}

