package com.example.shareeat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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
        bottomNav.setOnNavigationItemSelectedListener(navListener);
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
                        case R.id.navigation_favorites:
                            selectedFragment= new favorites();
                            break;
                        case R.id.navigation_profile:
                            selectedFragment= new profile();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_navhost,selectedFragment).commit();
                    return true;
                }
    };



}
