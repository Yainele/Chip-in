package com.example.chipinv01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class Homepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homegage);
        Objects.requireNonNull(getSupportActionBar()).hide();
        //

        //Bottom nav
        BottomNavigationView HomepageBottomNav = findViewById(R.id.bottomNavigationView);
        HomepageBottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new HomeFragment()).commit();

    }
    //Listener Nav Bar
    @SuppressLint("NonConstantResourceId")
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;

        switch (item.getItemId()){
            case R.id.item1:
                selectedFragment = new HomeFragment();
                break;
            case R.id.item2:
                selectedFragment = new NewRecordFragment();
                break;
            case R.id.item3:
                selectedFragment = new ProfileFragment();
                break;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout,selectedFragment)
                .commit();
        return true;
    };


}