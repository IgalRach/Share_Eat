package com.example.shareeat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

import java.util.LinkedList;
import java.util.List;
import java.util.NavigableMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class AllPosts extends Fragment {
    List<Recipe> data = new LinkedList<Recipe>();
    ProgressBar pb;
    Button addBtn;
    RecyclerView list;
    RecipesAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_all_posts, container, false);
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

        adapter = new RecipesAdapter();
        list.setAdapter(adapter);

        adapter.setOnClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("TAG", "row was clicked "+position);
            }
        });
        reloadData();
        return view;
    }

    public void reloadData(){
        pb.setVisibility(View.VISIBLE);
        Model.instance.getAllRecipes(new Model.GetAllRecipesListener() {
            @Override
            public void onComplete(List<Recipe> data2) {
                 data = data2;
                for(Recipe recipe:data2){
                    Log.d("Tag","recipe id: "+ recipe.getId());
                }
                pb.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
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
            Recipe recipe = data.get(position);
            holder.bindData(recipe, position);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }


    }

}

