package com.cam.goforlunch.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cam.goforlunch.R;
import com.cam.goforlunch.model.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantItemAdapter extends RecyclerView.Adapter <RestaurantItemAdapter.RestaurantsViewHolder> {

    // Interface to configure a listener on RecyclerView items
    public interface RecyclerViewOnClickListener {

        void recyclerViewOnClick(int position);
    }

    private final List<Restaurant> restaurantsList;
    private final RecyclerViewOnClickListener listener;
    private List<Restaurant> restaurants;

    public void updateRestaurants(@NonNull final List<Restaurant> restaurants) {
        this.restaurants = restaurants;
        notifyDataSetChanged();
    }


    public RestaurantItemAdapter(List<Restaurant> restaurantsList, RecyclerViewOnClickListener listener) {

        this.restaurantsList = restaurantsList;
        this.listener = listener;

    }

    @NonNull
    @Override
    public RestaurantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);

        return new RestaurantsViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantsViewHolder viewHolder, int position) {

        viewHolder.populateViewHolder(this.restaurantsList.get(position));
    }

    @Override
    public int getItemCount() {

        return restaurantsList.size();
    }

    class RestaurantsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.restaurant_image)
        ImageView restaurantImage;
        @BindView(R.id.restaurant_workmates)
        ImageView restaurantWorkmates;
        @BindView(R.id.restaurant_star_1)
        ImageView restaurantStar1;
        @BindView(R.id.restaurant_star_2)
        ImageView restaurantStar2;
        @BindView(R.id.restaurant_star_3)
        ImageView restaurantStar3;
        @BindView(R.id.restaurant_name)
        TextView restaurantName;
        @BindView(R.id.restaurant_address)
        TextView restaurantAddress;
        @BindView(R.id.restaurant_hours)
        TextView restaurantHours;
        @BindView(R.id.restaurant_distance)
        TextView restaurantDistance;
        @BindView(R.id.restaurant_workmates_number)
        TextView restaurantWorkmatesNumber;

        final RecyclerViewOnClickListener recyclerViewOnClickListener;

        RestaurantsViewHolder(View view, RecyclerViewOnClickListener listener) {
            super(view);
            ButterKnife.bind(this, view);
            this.recyclerViewOnClickListener = listener;
            view.setOnClickListener(this);
        }

        // Update UI with text and image
        void populateViewHolder(Restaurant restaurant) {

            restaurantName.setText(restaurant.getName());
            restaurantAddress.setText(restaurant.getAddress());
            restaurantDistance.setText(restaurant.getDistance());
            restaurantWorkmatesNumber.setText(String.valueOf(restaurant.getNbWorkmates()));

            // Set text depending on restaurant's opening hours
            if (restaurant.getIsOpenNow() == null) {
                restaurantHours.setText(R.string.no_hours);
            } else if (restaurant.getIsOpenNow().equals("true")) {
                restaurantHours.setText(R.string.open);
                restaurantHours.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.Open));
            } else {
                restaurantHours.setText(R.string.closed);
                restaurantHours.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.Closed));
            }

            // Set restaurant image
            Picasso.get().load(restaurant.getImageUrl()).into(restaurantImage);

            // Display stars depending on restaurant's rating
            restaurantStar1.setVisibility(View.VISIBLE);
            restaurantStar2.setVisibility(View.VISIBLE);
            restaurantStar3.setVisibility(View.VISIBLE);
            if (restaurant.getRating() < 4.0)
                restaurantStar3.setVisibility(View.GONE);
            else if (restaurant.getRating() < 3.0)
                restaurantStar2.setVisibility(View.GONE);
            else if (restaurant.getRating() < 2.0)
                restaurantStar1.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {

            recyclerViewOnClickListener.recyclerViewOnClick(getAdapterPosition());
        }
    }
}