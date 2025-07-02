package com.example.dreamweaver_refactor;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import com.example.dreamweaver_refactor.databinding.ActivityMainBinding;
import com.example.dreamweaver_refactor.navigationbar.HomeFragment;
import com.example.dreamweaver_refactor.navigationbar.LogFragment;
import com.example.dreamweaver_refactor.navigationbar.SettingsFragment;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {

    private Fragment homeFragment;
    private Fragment clipboardFragment;
    private Fragment aiFragment;
    private Fragment settingsFragment;
    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize fragments once
        homeFragment = new HomeFragment();
        clipboardFragment = new LogFragment();
        aiFragment = new AIFragment();
        settingsFragment = new SettingsFragment();

        // Add them all and hide except the default
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, settingsFragment, "4").hide(settingsFragment)
                .add(R.id.fragment_container, aiFragment, "3").hide(aiFragment)
                .add(R.id.fragment_container, clipboardFragment, "2").hide(clipboardFragment)
                .add(R.id.fragment_container, homeFragment, "1")
                .commit();

        activeFragment = homeFragment;

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.home_navigation) selectedFragment = homeFragment;
            else if (item.getItemId() == R.id.clipboard_navigation) selectedFragment = clipboardFragment;
            else if (item.getItemId() == R.id.AI_navigation) selectedFragment = aiFragment;
            else if (item.getItemId() == R.id.settings_navigation) selectedFragment = settingsFragment;

            if (selectedFragment != null && selectedFragment != activeFragment) {
                getSupportFragmentManager().beginTransaction()
                        .hide(activeFragment)
                        .show(selectedFragment)
                        .commit();
                activeFragment = selectedFragment;
            }
            return true;
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Myref =database.getReference("message");

        Myref.setValue("hello nabil");
    }

}