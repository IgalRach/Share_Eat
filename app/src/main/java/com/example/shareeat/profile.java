package com.example.shareeat;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.shareeat.model.Model;
import com.example.shareeat.model.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class profile extends Fragment {

    RecipeViewModel viewModelProfile;
    ProgressBar pbProfile;
    RecyclerView listProfile;
    RecipesAdapter adapterProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        viewModelProfile = new ViewModelProvider(this).get(RecipeViewModel.class);

        TextView nameUser = view.findViewById(R.id.profile_title);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        nameUser.setText(user.getDisplayName());

        Button signOut = view.findViewById(R.id.signoutBtn);
        Button editProfileBtn= view.findViewById(R.id.profile_Edit_Btn);

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_profile_to_editProfile );
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();

                startActivity(new Intent(getActivity(), login.class));
            }
        });

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        listProfile = view.findViewById(R.id.profile_list);
        listProfile.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        listProfile.setLayoutManager(layoutManager);

        adapterProfile = new RecipesAdapter();
        listProfile.setAdapter(adapterProfile);

        adapterProfile.setOnClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Log.d("TAG", "row was clicked " + viewModelProfile.getData().getValue().get(position).getTitleRecipe());
            }
        });

        viewModelProfile.getData().observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                adapterProfile.notifyDataSetChanged();
            }
        });
        reloadData();
        return view;
    }

    public void reloadData(){
//        pbProfile.setVisibility(View.VISIBLE);
        Model.instance.refreshAllRecipes(new Model.GetAllRecipesListener() {
            @Override
            public void onComplete() {
               // pbProfile.setVisibility(View.INVISIBLE);
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
            Recipe recipe = viewModelProfile.getData().getValue().get(position);
            holder.bindData(recipe, position);
        }

        @Override
        public int getItemCount() {
            if (viewModelProfile.getData().getValue() == null){
                return 0; }
            return viewModelProfile.getData().getValue().size();
        }


    }
}