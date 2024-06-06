package com.example.toytrade2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.content.SharedPreferences;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);
        int currentUserId = sharedPreferences.getInt("current_user_id", -1);
        if (currentUserId == -1) {
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
        }
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Fragment defaultFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,
                defaultFragment).commit();
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    switch (menuItem.getItemId()) {
                        case R.id.mainPage:
                            selectedFragment = new MainFragment();
                            break;
                        case R.id.myToysPage:
                            selectedFragment = new MyToysFragment();
                            break;
                        case R.id.myChangesPage:
                            selectedFragment = new MyChangesFragment();
                            break;
                        case R.id.myProfilePage:
                            selectedFragment = new MyProfileFragment();
                            break;
                    }
                    assert selectedFragment != null;
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,
                            selectedFragment).commit();
                    return true;
                }
            };
}