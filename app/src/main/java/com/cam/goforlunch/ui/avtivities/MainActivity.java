package com.cam.goforlunch.ui.avtivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.cam.goforlunch.R;
import com.cam.goforlunch.ui.fragments.MapsFragment;
import com.cam.goforlunch.ui.fragments.RestaurantListFragment;
import com.cam.goforlunch.ui.fragments.WorksmatesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.activity_main_page_bottom_nav_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.bottom_list_view);

    }

    RestaurantListFragment restaurantListFragments = new RestaurantListFragment();
    MapsFragment mapsFragments = new MapsFragment();
    WorksmatesFragment worksmatesFragments = new WorksmatesFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.bottom_list_view:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, restaurantListFragments).commit();
                return true;

            case R.id.bottom_map:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mapsFragments).commit();
                return true;

            case R.id.bottom_workmates:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, worksmatesFragments).commit();
                return true;
        }
        return false;
    }
}