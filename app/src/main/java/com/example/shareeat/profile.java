package com.example.shareeat;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.shareeat.model.Model;
import com.example.shareeat.model.ModelFirebase;
import com.example.shareeat.model.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class profile extends Fragment {

    String userId;
    RecyclerView listProfile;

    List<Recipe> data = new LinkedList<Recipe>();
    profile.MyRecipesAdapter adapterProfile;
    MyRecipeViewModel viewModelProfile;
    LiveData<List<Recipe>> liveData;
    ProgressBar pbProfile;

    public profile(){}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        viewModelProfile = new ViewModelProvider(this).get(MyRecipeViewModel.class);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        TextView nameUser = view.findViewById(R.id.profile_title);
        nameUser.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
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

        listProfile = view.findViewById(R.id.profile_list);
        listProfile.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        listProfile.setLayoutManager(layoutManager);

        adapterProfile = new MyRecipesAdapter();
        listProfile.setAdapter(adapterProfile);

        adapterProfile.setOnClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Recipe recipe = data.get(position);
                profileDirections.ActionProfileToRecipeDetails direction = profileDirections.actionProfileToRecipeDetails(recipe.getId());
                Navigation.findNavController(getActivity(), R.id.mainactivity_navhost).navigate(direction);
                Log.d("TAG", "row was clicked " + data.get(position));
            }
        });

        liveData = viewModelProfile.getDataByUser(userId);
        liveData.observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                List<Recipe> reverseData = reverseData(recipes);
                data = reverseData;
                adapterProfile.notifyDataSetChanged();
                }
        });

        final SwipeRefreshLayout profileSwipe = view.findViewById(R.id.recipeProfile_list_swipe);
        profileSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Model.instance.refreshAllRecipes(new Model.GetAllRecipesListener() {
                    @Override
                    public void onComplete() {
                        profileSwipe.setRefreshing(false);
                    }
                });

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

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //    public void reloadData(){
////        pbProfile.setVisibility(View.VISIBLE);
//        Model.instance.getAllRecipesPerUser2(currentUser.getUid()) {
//            @Override
//            public void onComplete(List<Recipe> list) {
//                adapterProfile.notifyDataSetChanged();
//            }
//        });
//    }

    /////////////////////////////////////////////////////////Class ViewHolder
    static class MyRecipesViewHolder extends RecyclerView.ViewHolder{

        public OnItemClickListener listener;
        CircleImageView profilePic;
        TextView nickname;
        TextView recipeTitle;
        TextView category;
        ImageView postImg;

        public MyRecipesViewHolder(@NonNull View itemView, final profile.OnItemClickListener listener) {
            super(itemView);
            profilePic=itemView.findViewById(R.id.profile_profile_im);
            nickname = itemView.findViewById(R.id.listRow_nickname);
            recipeTitle=itemView.findViewById(R.id.listRow_titleRec);
            category= itemView.findViewById(R.id.listRow_category);
            postImg=itemView.findViewById(R.id.listRow_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        Log.d("Tag","position is: "+ position);
                        if (position != RecyclerView.NO_POSITION)
                            listener.onItemClick(position);
                    }
                }
            });
        }

        public void bindData(Recipe recipe) {
            nickname.setText(recipe.getUserName());
            recipeTitle.setText(recipe.getTitleRecipe());
            category.setText(recipe.getCategory());
        }
    }

    interface OnItemClickListener {
        void onItemClick(int position);
    }

    /////////////////////////////////////////////////////////Class Adapter
    class MyRecipesAdapter extends RecyclerView.Adapter<MyRecipesViewHolder> {

        private OnItemClickListener listener;

        void setOnClickListener(OnItemClickListener listener){
            this.listener = listener;
        }
        @NonNull
        @Override
        public MyRecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //View view = getLayoutInflater().inflate(R.layout.list_row, null);
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.list_row, parent, false);
            profile.MyRecipesViewHolder user = new profile.MyRecipesViewHolder(view, listener);
            return user;
        }

        @Override
        public void onBindViewHolder(@NonNull MyRecipesViewHolder holder, int position) {
            Recipe recipe = data.get(position);
            holder.bindData(recipe);

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