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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.shareeat.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class result extends Fragment {
    String category;
    RecyclerView list;
    List<Recipe> data = new LinkedList<>();
    RecipesAdapter adapter;
    RecipeViewModel viewModel;
    LiveData<List<Recipe>> liveData;
    ProgressBar pb;
    TextView noResults;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        viewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        pb= view.findViewById(R.id.result_pb);
        noResults= view.findViewById(R.id.result_txtView);
        category = resultArgs.fromBundle(getArguments()).getCategory();
        Log.d("TAG","arg_category: "+category);

        pb.setVisibility(View.VISIBLE);
        noResults.setVisibility(View.INVISIBLE);

        list= view.findViewById(R.id.result_recycler);
        list.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);

        adapter = new  RecipesAdapter();
        list.setAdapter(adapter);

        adapter.setOnClickListener( new result.OnItemClickListener(){

            @Override
            public void onItemClick(int position) {
                Recipe recipe = data.get(position);
                resultDirections.ActionResultToRecipeDetails direction = resultDirections.actionResultToRecipeDetails(recipe.getId());
                Navigation.findNavController(getActivity(), R.id.result_recycler).navigate(direction);
                Log.d("TAG", "row was clicked " + viewModel.getData().getValue().get(position).getTitleRecipe());
            }
        });

        liveData = viewModel.getRecipesByCategory(category);
        liveData.observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                List<Recipe> reversedData = reverseData(recipes);
                data = reversedData;
                pb.setVisibility(View.INVISIBLE);
                if(data.size()==0){
                    noResults.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }

        });



        return view;
    }


    private List<Recipe> reverseData(List<Recipe> recipes) {
        List<Recipe> reversedData = new LinkedList<>();
        for (Recipe recipe: recipes) {
            reversedData.add(0, recipe);
        }
        return reversedData;
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
            postImg.setImageResource(R.drawable.recipe_placeholder);
            if(recipe.getImageUrl()!=null){
                Picasso.get().load(recipe.getImageUrl()).placeholder(R.drawable.recipe_placeholder).into(postImg);
            }
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
        public result.RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //View view = getLayoutInflater().inflate(R.layout.list_row, null);
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.list_row, parent, false);
            result.RecipesViewHolder holder = new result.RecipesViewHolder(view);
            holder.listener = listener;
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {
            Recipe recipe = data.get(position);
            holder.bindData(recipe, position);
//            holder.nickname.setText(recipe.getUserName());
//            holder.category.setText(recipe.getCategory());
//            //holder.recipeTV.setText(recipe.getRecipe());
//            holder.recipeTitle.setText(recipe.getTitleRecipe());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }


    }
}