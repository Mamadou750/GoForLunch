package com.cam.goforlunch.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.cam.goforlunch.BuildConfig;
import com.cam.goforlunch.R;
import com.cam.goforlunch.data.UserRepository;
import com.cam.goforlunch.model.Restaurant;
import com.cam.goforlunch.model.User;
import com.cam.goforlunch.ui.fragments.MapsFragment;
import com.cam.goforlunch.ui.fragments.RestaurantListFragment;
import com.cam.goforlunch.ui.fragments.WorksmatesFragment;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.activity_main_page_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.main_page_toolbar)
    Toolbar mainToolbar;
    @BindView(R.id.activity_main_page_nav_view)
    NavigationView navigationView;
    @BindView(R.id.activity_main_page_bottom_nav_view)
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


        navigationView.setNavigationItemSelectedListener(this);
        configureDrawerLayout();
        configureBottomNavigationView();


    }


    // Configure menu in the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_page_menu, menu);

        return true;
    }


    // Configure Bottom Navigation View and display fragments
    private void configureBottomNavigationView() {

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            Fragment selectedFragment = null;

            switch (menuItem.getItemId()) {

                case R.id.bottom_map:
                    selectedFragment = new MapsFragment();
                    break;
                case R.id.bottom_list_view:
                    selectedFragment = new RestaurantListFragment();
                    break;
                case R.id.bottom_workmates:
                    selectedFragment = new WorksmatesFragment();
            }

            // Display fragment depending on item selected
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, Objects.requireNonNull(selectedFragment)).commit();

            return true;
        });
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
        UserRepository.getUser(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).addOnSuccessListener(documentSnapshot -> {
            User currentUser = documentSnapshot.toObject(User.class);
            Restaurant restaurant = Objects.requireNonNull(currentUser).getChosenRestaurant();
            Intent intent = new Intent(MainActivity.this, RestaurantDetailsActivity.class);
            intent.putExtra("RESTAURANT", restaurant);
            startActivity(intent);
        });

    }

    // Log out user and update UI
    private void signOutFromFirebase() {

    }


}

