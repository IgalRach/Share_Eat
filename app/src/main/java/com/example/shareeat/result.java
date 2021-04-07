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
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.shareeat.adapters.RecipesAdapter;
import com.example.shareeat.model.Model;
import com.example.shareeat.model.Recipe;
import java.util.LinkedList;
import java.util.List;

public class result extends Fragment {

    String category;
    RecyclerView listResults;
    RecipesAdapter adapterResults;
    RecipeViewModel viewModelResults;
    SwipeRefreshLayout resultsRefreshSwipe;
    TextView noResults;
    ProgressBar pb;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModelResults = new ViewModelProvider(this).get(RecipeViewModel.class);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //pb= view.findViewById(R.id.result_pb);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        category = resultArgs.fromBundle(getArguments()).getCategory();
        Log.d("TAG","arg_category: "+category);

        noResults= view.findViewById(R.id.result_txtView);
        noResults.setVisibility(View.INVISIBLE);

        listResults= view.findViewById(R.id.result_recycler);
        listResults.setHasFixedSize(true);

        resultsRefreshSwipe = view.findViewById(R.id.recipeResults_list_swipe);
        resultsRefreshSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Model.instance.refreshAllRecipes(new Model.GetAllRecipesListener() {
                    @Override
                    public void onComplete() {
                        resultsRefreshSwipe.setRefreshing(false);
                    }
                });

            }
        });

        viewModelResults.getRecipesByCategory(category).observe(getViewLifecycleOwner(), recipeUpdateObserver);
        return view;
    }

    Observer<List<Recipe>> recipeUpdateObserver = new Observer<List<Recipe>>() {
        @Override
        public void onChanged(List<Recipe> recipeArrayList) {
            List<Recipe> data = new LinkedList<Recipe>();

            for (Recipe recipe: recipeArrayList)
                data.add(0, recipe);
            recipeArrayList = data;

            if(data.size()==0){
                noResults.setVisibility(View.VISIBLE);
            }

            List<Recipe> finalRecipeArrayList = recipeArrayList;
            adapterResults = new RecipesAdapter(getContext(), recipeArrayList, new RecipesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Recipe rec = finalRecipeArrayList.get(position);
                    String recipeId = rec.getId();
                    resultDirections.ActionResultToRecipeDetails direction = resultDirections.actionResultToRecipeDetails(recipeId);
                    Navigation.findNavController(getActivity(), R.id.mainactivity_navhost).navigate(direction);
                    Log.d("TAG", "row was clicked " + recipeId);
                }
            });
            listResults.setLayoutManager(new LinearLayoutManager(getContext()));
            listResults.setAdapter(adapterResults);
        }
    };
}