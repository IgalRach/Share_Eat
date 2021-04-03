package com.example.shareeat;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.example.shareeat.model.Model;
import com.example.shareeat.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;
import java.util.NavigableMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class AllPosts extends Fragment {

    RecyclerView list;
    List<Recipe> data = new LinkedList<Recipe>();
    AllPosts.RecipesAdapter adapter;
    RecipeViewModel viewModel;
    LiveData<List<Recipe>> liveData;

    Button addBtn;
    SwipeRefreshLayout sref;
    ProgressBar pb;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
    }

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

        // Swipe to refresh
        sref = view.findViewById(R.id.meal_list_swipe);
        sref.setOnRefreshListener(() -> {
            sref.setRefreshing(true);
            pb.setVisibility(View.VISIBLE);
            //fab.setEnabled(false);
            Model.instance.refreshAllRecipes(new Model.GetAllRecipesListener() {
                @Override
                public void onComplete() {
                    sref.setRefreshing(false);
                    pb.setVisibility(View.INVISIBLE);
                    //fab.setEnabled(true);
                }
            });
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
                Recipe recipe = data.get(position);
                AllPostsDirections.ActionAllPostsToRecipeDetails direction = AllPostsDirections.actionAllPostsToRecipeDetails(recipe.getId());
                Navigation.findNavController(getActivity(), R.id.mainactivity_navhost).navigate(direction);
                Log.d("TAG", "row was clicked " + viewModel.getData().getValue().get(position).getTitleRecipe());
            }
        });

        liveData = viewModel.getData();
        liveData.observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                List<Recipe> reverseData = reverseData(recipes);
                data = reverseData;
                adapter.notifyDataSetChanged();
            }
        });

        reverseData(data);
        //reloadData();
        return view;
    }

    private List<Recipe> reverseData(List<Recipe> recipes) {
        List<Recipe> reversedData = new LinkedList<>();
        for (Recipe recipe: recipes) {
            reversedData.add(0, recipe);
        }
        return reversedData;
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
        TextView recipeTV;
        TextView category;
        ImageView postImg;
        int position;
        Recipe myRecipe;

        public RecipesViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic=itemView.findViewById(R.id.profile_profile_im);
            nickname = itemView.findViewById(R.id.listRow_nickname);
            recipeTitle=itemView.findViewById(R.id.listRow_titleRec);
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
            category.setText(recipe.getCategory());
            this.position= position;
            myRecipe = recipe;
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
            //View view = getLayoutInflater().inflate(R.layout.list_row, null);
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.list_row, parent, false);
            AllPosts.RecipesViewHolder holder = new AllPosts.RecipesViewHolder(view);
            holder.listener = listener;
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {
            Recipe recipe = data.get(position);
            holder.bindData(recipe, position);
            holder.nickname.setText(recipe.getUserName());
            holder.category.setText(recipe.getCategory());
            //holder.recipeTV.setText(recipe.getRecipe());
            holder.recipeTitle.setText(recipe.getTitleRecipe());

            holder.postImg.setImageResource(R.drawable.recipe_placeholder);
            if(recipe.getImageUrl()!=null){
                Picasso.get().load(recipe.getImageUrl()).placeholder(R.drawable.recipe_placeholder).into(holder.postImg);
            }

        }

        @Override
        public int getItemCount() {
            return data.size();
        }


    }

}

