package com.example.shareeat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.icu.text.UFormat;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity/* implements AdapterView.OnItemSelectedListener*/ {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


//        ListView list = findViewById(R.id.main_list_v);
//        MyAdapter adapter = new MyAdapter();
//        list.setAdapter(adapter);

//        Spinner addPost_spinner= findViewById(R.id.addPost_Category);
//        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this,R.array.Categories, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        addPost_spinner.setAdapter(adapter);
//        addPost_spinner.setOnItemSelectedListener(this);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            selectedFragment = new AllPosts();
                            break;
                        case R.id.navigation_search:
                            selectedFragment = new Fragment_search();
                            break;
//                        case R.id.navigation_favorites:
//                            selectedFragment= new AllPosts();
//                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment,selectedFragment).commit();
                    return true;
                }
            };


//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        String addPost_text= parent.getItemAtPosition(position).toString();
//        Toast.makeText(parent.getContext(),addPost_text,Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }

//    class MyAdapter extends  BaseAdapter{
//        List<String> data = new LinkedList<String>();
//
//        public MyAdapter() {
//            for (int i = 0; i < 10; i++) {
//                data.add("element #"+i);
//            }
//        }
//        @Override
//        public int getCount() {
//            return data.size();
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup) {
//            View v = getLayoutInflater().inflate(R.layout.list_row,null);
//            String str = data.get(i);
//            TextView txt = v.findViewById(R.id.listRow_nickname);
//            txt.setText(str);
//            TextView recipe = v.findViewById(R.id.listRow_recipe);
//            txt.setText((CharSequence) recipe);
//            TextView title = v.findViewById(R.id.listRow_titleRec);
//            txt.setText((CharSequence) title);
//            return v;
//        }
//    }

}
