package com.cam.goforlunch.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cam.goforlunch.R;
import com.cam.goforlunch.data.UserRepository;
import com.cam.goforlunch.model.Restaurant;
import com.cam.goforlunch.model.User;
import com.cam.goforlunch.ui.ViewModel.DetailRestaurantViewModel;
import com.cam.goforlunch.ui.ViewModel.DetailRestaurantViewModelFactory;
import com.cam.goforlunch.ui.ViewModel.UserViewModelFactory;
import com.cam.goforlunch.ui.ViewModel.WorksmatesViewModel;
import com.cam.goforlunch.ui.adapter.DetailsWorkmatesAdapter;
import com.cam.goforlunch.ui.adapter.WorksmateItemAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantDetailsActivity extends AppCompatActivity {

    @BindView(R.id.restaurant_details_name)
    TextView restaurantName;
    @BindView(R.id.restaurant_details_type_address)
    TextView restaurantAddress;
    @BindView(R.id.restaurant_details_image)
    ImageView restaurantImage;
    @BindView(R.id.check_floating_action_button)
    FloatingActionButton checkFloatingActionButton;
    @BindView(R.id.uncheck_floating_action_button)
    FloatingActionButton uncheckFloatingActionButton;
    @BindView(R.id.call_constraint_layout)
    View callButton;
    @BindView(R.id.like_constraint_layout)
    View likeButton;
    @BindView(R.id.unlike_constraint_layout)
    View unlikeButton;
    @BindView(R.id.website_constraint_layout)
    View websiteButton;
    @BindView(R.id.restaurant_details_star_1)
    ImageView restaurantStar1;
    @BindView(R.id.restaurant_details_star_2)
    ImageView restaurantStar2;
    @BindView(R.id.restaurant_details_star_3)
    ImageView restaurantStar3;
    private DetailRestaurantViewModel viewModel;
    private  DetailsWorkmatesAdapter adapter ;


    private ArrayList<User> lUsers = new ArrayList<>();
    private ArrayList<User> workmateList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
        viewModel = new ViewModelProvider(this, DetailRestaurantViewModelFactory.getFactoryDetailsRestaurant()).get(DetailRestaurantViewModel.class);
        adapter = new DetailsWorkmatesAdapter(new ArrayList<>(),this);
        Intent intent = getIntent();
        Restaurant restaurant = (Restaurant) intent.getSerializableExtra("RESTAURANT");
        viewModel.fetchRestaurantDetails(restaurant, restaurant.getPlaceId());
        viewModel.getRestaurant().observe(this, restaurantDetail ->{
            updateData(restaurantDetail);
        } );

        ButterKnife.bind(this);



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    // Update activity with restaurant data
    private void updateData(Restaurant restaurantDetail) {


        restaurantName.setText(Objects.requireNonNull(restaurantDetail).getName());
        restaurantAddress.setText(restaurantDetail.getAddress());

        Picasso.get().load(restaurantDetail.getImageUrl()).into(restaurantImage);

        // Display stars depending on restaurant's rating
        if (restaurantDetail.getRating() < 4.0)
            restaurantStar3.setVisibility(View.GONE);
        else if (restaurantDetail.getRating() < 3.0)
            restaurantStar2.setVisibility(View.GONE);
        else if (restaurantDetail.getRating() < 2.0)
            restaurantStar1.setVisibility(View.GONE);

        setFloatingActionButton(restaurantDetail);
        // Set buttons
        addWorkmates(restaurantDetail);
        setCallButton(restaurantDetail);
        setLikeButton(restaurantDetail);
        setWebsiteButton(restaurantDetail);

        configureRecyclerView();

    }

    // Set floating action buttons
    private void setFloatingActionButton(Restaurant restaurant) {

        // Verify if restaurant is currently chosen, then set buttons state accordingly
        UserRepository.getUser(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .addOnSuccessListener(documentSnapshot -> {

                    User currentUser = documentSnapshot.toObject(User.class);
                    if (Objects.requireNonNull(currentUser).getChosenRestaurant() != null
                            && currentUser.getChosenRestaurant().getPlaceId().equals(restaurant.getPlaceId())) {

                        checkFloatingActionButton.hide();
                        uncheckFloatingActionButton.show();
                    } else {
                        uncheckFloatingActionButton.hide();
                        checkFloatingActionButton.show();
                    }
                });

        // Add chosen restaurant to database and switch buttons
        checkFloatingActionButton.setOnClickListener(v -> UserRepository.updateChosenRestaurant(FirebaseAuth.getInstance().getCurrentUser().getUid(), restaurant)
                .addOnSuccessListener(aVoid -> {
                    viewModel.addRemoveFromList(checkFloatingActionButton.isActivated());
                    checkFloatingActionButton.hide();
                    uncheckFloatingActionButton.show();
                }));

        // Remove restaurant from database and switch buttons
        uncheckFloatingActionButton.setOnClickListener(v ->UserRepository.updateChosenRestaurant(FirebaseAuth.getInstance().getCurrentUser().getUid(), null)
                .addOnSuccessListener(aVoid -> {
                    viewModel.addRemoveFromList(!checkFloatingActionButton.isActivated());
                    uncheckFloatingActionButton.hide();
                    checkFloatingActionButton.show();
                }));
    }


    // Set call button
    private void setCallButton(Restaurant restaurant) {

        callButton.setOnClickListener(v -> {

            if (restaurant.getPhoneNumber() != null) {

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + restaurant.getPhoneNumber()));
                startActivity(callIntent);
            } else {

                Toast.makeText(v.getContext(), "There's no phone number found for this restaurant", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Set like button
    private void setLikeButton(Restaurant restaurant) {

        // Verify if restaurant is currently chosen, then set buttons state accordingly
        UserRepository.getUser(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .addOnSuccessListener(documentSnapshot -> {

                    User currentUser = documentSnapshot.toObject(User.class);
                    if (Objects.requireNonNull(currentUser).getLikedRestaurants() != null) {

                        for (Restaurant likedRestaurant : currentUser.getLikedRestaurants()) {
                            if (likedRestaurant.getPlaceId().equals(restaurant.getPlaceId())) {
                                likeButton.setVisibility(View.INVISIBLE);
                                unlikeButton.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        unlikeButton.setVisibility(View.INVISIBLE);
                        likeButton.setVisibility(View.VISIBLE);
                    }
                });

        likeButton.setOnClickListener(v -> {

            // Verify if restaurant isn't already liked, then like it
            UserRepository.addLikedRestaurant(FirebaseAuth.getInstance().getCurrentUser().getUid(), restaurant)
                    .addOnSuccessListener(aVoid -> {

                        likeButton.setVisibility(View.INVISIBLE);
                        unlikeButton.setVisibility(View.VISIBLE);
                    });
        });

        unlikeButton.setOnClickListener(v -> UserRepository.removeLikedRestaurant(FirebaseAuth.getInstance().getCurrentUser().getUid(), restaurant)
                .addOnSuccessListener(aVoid -> {

                    unlikeButton.setVisibility(View.INVISIBLE);
                    likeButton.setVisibility(View.VISIBLE);
                }));
    }

    // Set website button
    private void setWebsiteButton(Restaurant restaurant) {

        websiteButton.setOnClickListener(v -> {

            if (restaurant.getWebsite() != null) {

                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurant.getWebsite()));
                startActivity(webIntent);
            } else {

                Toast.makeText(v.getContext(), "There's no website found for this restaurant", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Configure RecyclerView to display articles
    private void configureRecyclerView() {

        RecyclerView recyclerView = findViewById(R.id.restaurant_details_recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void addWorkmates(Restaurant restaurant) {
        viewModel.getChousenUsers(restaurant).observe(this, list ->{
            updateUser(list);
        } );

    }


    private void updateUser(List<User> users) {
        lUsers = (ArrayList<User>) users;
        adapter.updateUsers(lUsers);
    }

}