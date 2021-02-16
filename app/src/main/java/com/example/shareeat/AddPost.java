package com.example.shareeat;

import android.graphics.Insets;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class AddPost extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_post, container, false);

        Button addBtn = view.findViewById(R.id.addPost_add_btn);
        addBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
            //    Navigation.findNavController(view).navigate(R.id.);
            }
        });
        return view;
    }
}