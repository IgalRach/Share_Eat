package com.example.shareeat;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shareeat.model.Recipe;

import java.util.LinkedList;
import java.util.List;

public class result extends AllPosts {
    String category;
    RecyclerView list;
    List<Recipe> data = new LinkedList<>();
    AllPosts.RecipesAdapter adapter;
    RecipeViewModel viewModel;
    LiveData<List<Recipe>> liveData;

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
//
//        category = resultArgs.fromBundle(getArguments()).getCategory();
//
//        list= view.findViewById(R.id.result_recycler);
//        list.setHasFixedSize(true);
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        list.setLayoutManager(layoutManager);
//
//        adapter = new  AllPosts.RecipesAdapter();
//        list.setAdapter(adapter);
//
//        liveData = viewModel.getDataByCategory(category);
//        liveData.observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
//            @Override
//            public void onChanged(List<Recipe> recipes) {
//
//                adapter.notifyDataSetChanged();
//            }
//        });

        return view;
    }
}