package com.cam.goforlunch.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.cam.goforlunch.BuildConfig;
import com.cam.goforlunch.R;
import com.cam.goforlunch.ui.fragments.MapsFragment;
import com.cam.goforlunch.ui.fragments.RestaurantListFragment;
import com.cam.goforlunch.ui.fragments.WorksmatesFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.activity_main_page_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.main_page_toolbar)
    Toolbar mainToolbar;
    @BindView(R.id.activity_main_page_nav_view)
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;


    private final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private static double currentLatitude;
    private static double currentLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mainToolbar);

        // Initialize Places SDK for Autocomplete
        if (!Places.isInitialized())
            Places.initialize(this, BuildConfig.MAPS_API_KEY);
        navigationView.setNavigationItemSelectedListener(this:: configureBottomNavigationView);
        navigationView.setNavigationItemSelectedListener(this:: onNavigationItemSelected);
        configureDrawerLayout();


        bottomNavigationView = findViewById(R.id.activity_main_page_bottom_nav_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.bottom_list_view);

    }


    // Configure menu in the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_page_menu, menu);

        return true;
    }

    RestaurantListFragment restaurantListFragments = new RestaurantListFragment();
    MapsFragment mapsFragments = new MapsFragment();
    WorksmatesFragment worksmatesFragments = new WorksmatesFragment();


    public boolean  configureBottomNavigationView(@NonNull MenuItem item) {

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

    private void configureDrawerLayout() {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                mainToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // Set a back button in the toolbar
    public void onBackPressed() {

        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Set search button to use Autocomplete
    public boolean onOptionsItemSelected(MenuItem item) {

        // Set list of needed data
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.TYPES, Place.Field.ADDRESS,
                Place.Field.RATING, Place.Field.PHONE_NUMBER, Place.Field.WEBSITE_URI, Place.Field.PHOTO_METADATAS);

        if (item.getItemId() == R.id.menu_activity_main_search) {
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                    .setLocationBias(RectangularBounds.newInstance(
                            new LatLng(currentLatitude - 0.1, currentLongitude - 0.1),
                            new LatLng(currentLatitude + 0.1, currentLongitude + 0.1)))
                    .setTypeFilter(TypeFilter.ESTABLISHMENT)
                    .build(this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            // Open restaurant details
            case R.id.main_page_drawer_lunch:
                startChosenRestaurantDetailsActivity();
                break;

            // Sign out from app
            case R.id.main_page_drawer_logout:
                signOutFromFirebase();
                break;
        }

        return false;
    }


    // Start selected RestaurantDetailsActivity
    private void startChosenRestaurantDetailsActivity() {


    }

    // Log out user and update UI
    private void signOutFromFirebase() {

    }


}