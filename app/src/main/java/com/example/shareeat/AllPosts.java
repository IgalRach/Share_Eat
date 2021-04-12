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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import com.example.shareeat.adapters.RecipesAdapter;
import com.example.shareeat.model.Model;
import com.example.shareeat.model.ModelFirebase;
import com.example.shareeat.model.Recipe;
import com.example.shareeat.model.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.LinkedList;
import java.util.List;


public class AllPosts extends Fragment {

    RecyclerView list;
    RecipesAdapter adapter;
    RecipeViewModel viewModel;
    SwipeRefreshLayout allPostsRefreshSwipe;
    Button addBtn;
    ProgressBar pb;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //pb= view.findViewById(R.id.allpost_progressBar);

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_all_posts, container, false);



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

        allPostsRefreshSwipe = view.findViewById(R.id.recipe_list_swipe);
        allPostsRefreshSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Model.instance.refreshAllRecipes(new Model.GetAllRecipesListener() {
                    @Override
                    public void onComplete() {
                        allPostsRefreshSwipe.setRefreshing(false);
                    }
                });

            }
        });

        viewModel.getData().observe(getViewLifecycleOwner(), recipeUpdateObserver);
        return view;
    }

    Observer<List<Recipe>> recipeUpdateObserver = new Observer<List<Recipe>>() {
        @Override
        public void onChanged(List<Recipe> recipeArrayList) {
            List<Recipe> data = new LinkedList<Recipe>();
                for (Recipe recipe: recipeArrayList)
                    data.add(0, recipe);

                recipeArrayList = data;
            List<Recipe> finalRecipeArrayList = recipeArrayList;
            adapter = new RecipesAdapter(getContext(), recipeArrayList, new RecipesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Recipe rec = finalRecipeArrayList.get(position);
                    String recipeId = rec.getId();
                    AllPostsDirections.ActionAllPostsToRecipeDetails direction = AllPostsDirections.actionAllPostsToRecipeDetails(recipeId);
                    Navigation.findNavController(getActivity(), R.id.mainactivity_navhost).navigate(direction);
                    Log.d("TAG", "row was clicked " + recipeId);
                }
            });
            list.setLayoutManager(new LinearLayoutManager(getContext()));
            list.setAdapter(adapter);
        }
    };
}