package com.example.shareeat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;


public class Fragment_search extends Fragment {

    Button Italian;
    Button French;
    Button Spicy;
    Button Meat;
    Button Fish;
    Button Dessert;
    Button Kosher;
    Button Dairy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        Italian= view.findViewById(R.id.search_italian);
        French= view.findViewById(R.id.search_french);
        Spicy= view.findViewById(R.id.search_spicy);
        Meat = view.findViewById(R.id.search_meat);
        Fish= view.findViewById(R.id.search_fish);
        Dessert= view.findViewById(R.id.search_desert);
        Kosher=view.findViewById(R.id.search_kosher);
        Dairy= view.findViewById(R.id.search_Dairy);

        Italian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_searchDirections.ActionFragmentSearchToResult action = Fragment_searchDirections.actionFragmentSearchToResult("Italian");
                Navigation.findNavController(view).navigate(action);
            }
        });

        French.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_searchDirections.ActionFragmentSearchToResult action = Fragment_searchDirections.actionFragmentSearchToResult("French");
                Navigation.findNavController(view).navigate(action);
            }
        });

        Spicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_searchDirections.ActionFragmentSearchToResult action = Fragment_searchDirections.actionFragmentSearchToResult("Spicy");
                Navigation.findNavController(view).navigate(action);
            }
        });

        Meat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_searchDirections.ActionFragmentSearchToResult action = Fragment_searchDirections.actionFragmentSearchToResult("Meat");
                Navigation.findNavController(view).navigate(action);
            }
        });

        Fish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_searchDirections.ActionFragmentSearchToResult action = Fragment_searchDirections.actionFragmentSearchToResult("Fish");
                Navigation.findNavController(view).navigate(action);
            }
        });

        Dessert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_searchDirections.ActionFragmentSearchToResult action = Fragment_searchDirections.actionFragmentSearchToResult("Dessert");
                Navigation.findNavController(view).navigate(action);
            }
        });

        Kosher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_searchDirections.ActionFragmentSearchToResult action = Fragment_searchDirections.actionFragmentSearchToResult("Kosher");
                Navigation.findNavController(view).navigate(action);
            }
        });

        Dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_searchDirections.ActionFragmentSearchToResult action = Fragment_searchDirections.actionFragmentSearchToResult("Dairy");
                Navigation.findNavController(view).navigate(action);
            }
        });
        return view;
    }
}