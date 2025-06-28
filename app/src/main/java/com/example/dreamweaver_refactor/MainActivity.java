package com.example.dreamweaver_refactor;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;


import com.example.dreamweaver_refactor.databinding.ActivityMainBinding;
import com.example.dreamweaver_refactor.navigationbar.AIFragment;
import com.example.dreamweaver_refactor.navigationbar.HomeFragment;
import com.example.dreamweaver_refactor.navigationbar.LogFragment;
import com.example.dreamweaver_refactor.navigationbar.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //implementation of navigation bar//
        ActivityMainBinding binding;
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();        // Set default fragment

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if(item.getItemId()==R.id.home_navigation){selectedFragment = new HomeFragment();}
                if(item.getItemId()==R.id.clipboard_navigation){selectedFragment = new LogFragment();}
                if(item.getItemId()==R.id.AI_navigation){selectedFragment = new AIFragment();}
                if(item.getItemId()==R.id.settings_navigation){selectedFragment = new SettingsFragment();}

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                }
                return true;
            }
        });

    }
}