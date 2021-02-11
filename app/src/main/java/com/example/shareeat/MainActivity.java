package com.example.shareeat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity/* implements AdapterView.OnItemSelectedListener*/ {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        Spinner addPost_spinner= findViewById(R.id.addPost_Category);
//        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this,R.array.Categories, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        addPost_spinner.setAdapter(adapter);
//        addPost_spinner.setOnItemSelectedListener(this);
    }

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
}