package com.example.shareeat;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import com.example.shareeat.adapters.RecipesAdapter;
import com.example.shareeat.model.Model;
import com.example.shareeat.model.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import java.util.LinkedList;
import java.util.List;

public class profile extends Fragment {

    String userId;
    RecyclerView listProfile;
    RecipesAdapter adapterProfile;
    RecipeViewModel viewModelProfile;
    SwipeRefreshLayout profileRefreshSwipe;
    Button signOut;
    Button editProfileBtn;
    TextView nameUser;

    public profile(){}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModelProfile = new ViewModelProvider(this).get(RecipeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        nameUser = view.findViewById(R.id.profile_title);
        nameUser.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        signOut = view.findViewById(R.id.signoutBtn);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();

                startActivity(new Intent(getActivity(), login.class));
            }
        });

        editProfileBtn= view.findViewById(R.id.profile_Edit_Btn);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_profile_to_editProfile );
            }
        });

        listProfile = view.findViewById(R.id.profile_list);
        listProfile.setHasFixedSize(true);

        profileRefreshSwipe = view.findViewById(R.id.recipeProfile_list_swipe);
        profileRefreshSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Model.instance.refreshAllRecipes(new Model.GetAllRecipesListener() {
                    @Override
                    public void onComplete() {
                        profileRefreshSwipe.setRefreshing(false);
                    }
                });

            }
        });

        viewModelProfile.getDataByUser(userId).observe(getViewLifecycleOwner(), recipeUpdateObserver);
        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    Observer<List<Recipe>> recipeUpdateObserver = new Observer<List<Recipe>>() {
        @Override
        public void onChanged(List<Recipe> recipeArrayList) {
            List<Recipe> data = new LinkedList<Recipe>();
            for (Recipe recipe: recipeArrayList)
                data.add(0, recipe);

            recipeArrayList = data;
            List<Recipe> finalRecipeArrayList = recipeArrayList;
            adapterProfile = new RecipesAdapter(getContext(), recipeArrayList, new RecipesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Recipe rec = finalRecipeArrayList.get(position);
                    String recipeId = rec.getId();
                    profileDirections.ActionProfileToRecipeDetails direction = profileDirections.actionProfileToRecipeDetails(recipeId);
                    Navigation.findNavController(getActivity(), R.id.mainactivity_navhost).navigate(direction);
                    Log.d("TAG", "row was clicked " + recipeId);
                }
            });
            listProfile.setLayoutManager(new LinearLayoutManager(getContext()));
            listProfile.setAdapter(adapterProfile);
        }
    };
}