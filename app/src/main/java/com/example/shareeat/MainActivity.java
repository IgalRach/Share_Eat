package com.example.shareeat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.shareeat.model.Model;
import com.example.shareeat.model.Recipe;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity<OnOption> extends AppCompatActivity {
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Navigation menu
        navController = Navigation.findNavController(this, R.id.mainactivity_navhost);
        NavigationUI.setupActionBarWithNavController(this,navController);

        //Navigation bar
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNav,navController);
        //Navigation bar End

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId()==android.R.id.home){
            navController.navigateUp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
