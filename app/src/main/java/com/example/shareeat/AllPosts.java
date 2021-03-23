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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.shareeat.adapters.RecipesAdapter;
import com.example.shareeat.model.Model;
import com.example.shareeat.model.Recipe;

import java.util.LinkedList;
import java.util.List;
import java.util.NavigableMap;


public class AllPosts extends Fragment {
    List<Recipe> recipeList = new LinkedList<Recipe>();
    ProgressBar pb;
    Button addBtn;
    RecipesAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_all_posts, container, false);



        pb= view.findViewById(R.id.allpost_progressBar);

        // added button from all post to add post.
        addBtn= view.findViewById(R.id.AllPost_addBtn);
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

        pb.setVisibility(View.INVISIBLE);
        reloadData();

 //--------------------------------------------------------------------------------------------


//--------------------------------------------------------------------------------------------


        adapter= new RecipesAdapter(getLayoutInflater());
        adapter.data=recipeList;
        rv.setAdapter(adapter);


        adapter.setOnItemClickListener(new RecipesAdapter.OnItemClickListener(){
            @Override
            public void OnItemClick(int position) {
                Log.d("Tag","row was clicked "+ position);
            }
        });

        return view;
    }

//    class  MyAdapter extends BaseAdapter{
//
//        @Override
//        public int getCount() {
//            return recipeList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int i, View convertView, ViewGroup viewGroup) {
//            if(convertView==null){
//                convertView= getLayoutInflater().inflate(R.layout.list_row,null);
//            }
//            TextView title= convertView.findViewById(R.id.listRow_titleRec);
//            TextView nickname= convertView.findViewById(R.id.listRow_nickname);
//            ImageView image= convertView.findViewById(R.id.listRow_img);
//            TextView recipetv= convertView.findViewById(R.id.listRow_recipe);
//
//            Recipe recipe=recipeList.get(i);
//            title.setText(recipe.getTitleRecipe());
//            nickname.setText(recipe.getUserName());
//           recipetv.setText(recipe.getRecipe());
//            return convertView;
//        }
//    }

    public void reloadData(){
        pb.setVisibility(View.VISIBLE);
        Model.instance.getAllRecipes(new Model.GetAllRecipesListener() {
            @Override
            public void onComplete(List<Recipe> data) {
                recipeList = data;
                for(Recipe recipe:data){
                    Log.d("Tag","recipe id: "+ recipe.getId());
                }
                pb.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
            }
        });
    }

}

