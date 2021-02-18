package com.example.shareeat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.shareeat.model.Recipe;

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



//        RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.main_list_v);
//        ListAdapter listAdapter = new ListAdapter() ;
//        recyclerView.setAdapter(listAdapter);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

//    class MyAdapter extends RecyclerView.Adapter{
//        @NonNull
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            return null;
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return 0;
//        }
//        private class ListViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
//            private ImageView profile_img;
//            private TextView nickName;
//            private EditText titleRecipe;
//            private EditText recipe;
//            private ImageView img;
//
//            public ListViewHolder(@NonNull View itemView) {
//                super(itemView);
//                titleRecipe=(EditText)itemView.findViewById(R.id.listRow_titleRec);
//                recipe = (EditText)itemView.findViewById(R.id.listRow_recipe);
//                img =(ImageView)itemView.findViewById(R.id.listRow_img);
//                itemView.setOnClickListener(this);
//            }
//
//            public void bindView(int position){
//                Recipe r=
//                titleRecipe.setText();
//            }
//
//
//            @Override
//            public void onClick(View v) {
//
//            }
//        }
//    }
}

