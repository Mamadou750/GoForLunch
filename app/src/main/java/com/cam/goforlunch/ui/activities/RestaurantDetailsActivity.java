package com.cam.goforlunch.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cam.goforlunch.R;
import com.cam.goforlunch.model.Restaurant;
import com.cam.goforlunch.model.User;
import com.cam.goforlunch.ui.ViewModel.DetailRestaurantViewModel;
import com.cam.goforlunch.ui.ViewModel.DetailRestaurantViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

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

    private List<User> workmateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        viewModel = new ViewModelProvider(this, DetailRestaurantViewModelFactory.getFactoryDetailsRestaurant()).get(DetailRestaurantViewModel.class);
        Intent intent = getIntent();
        Restaurant restaurant = (Restaurant) intent.getSerializableExtra("RESTAURANT");
        viewModel.fetchRestaurantDetails(restaurant,restaurant.getPlaceId());
        viewModel.getRestaurant().observe(this, restaurantDetail ->{
            updateData(restaurantDetail);
        } );

        ButterKnife.bind(this);



    }

    @Override
    public void onDestroy() {
        super.onDestroy();

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

        // Set buttons
       /* setFloatingActionButton(restaurantDetail);
        setCallButton(restaurantDetail);
        setLikeButton(restaurantDetail);
        setWebsiteButton(restaurantDetail);


        configureRecyclerView();*/

    }
}