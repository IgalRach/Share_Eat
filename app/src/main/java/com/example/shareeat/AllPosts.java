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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.shareeat.adapters.RecipesAdapter;
import com.example.shareeat.model.Model;
import com.example.shareeat.model.Recipe;

import java.util.List;
import java.util.NavigableMap;


public class AllPosts extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_all_posts, container, false);


        // added button from all post to add post.
        Button addBtn= view.findViewById(R.id.AllPost_addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_allPosts_to_addPost );
            }
        });

        RecyclerView rv = view.findViewById(R.id.main_recycler_v);
        rv.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);


        List<Recipe> data= Model.instance.getAllRecipes();

        RecipesAdapter adapter= new RecipesAdapter(getLayoutInflater());
        adapter.data=data;
        rv.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecipesAdapter.OnItemClickListener(){
            @Override
            public void OnItemClick(int position) {
                Log.d("Tag","row was clicked "+ position);
            }
        });

        return view;
    }

}

