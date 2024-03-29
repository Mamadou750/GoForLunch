package com.cams.goforlunch.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cams.goforlunch.R;
import com.cams.goforlunch.model.Restaurant;
import com.cams.goforlunch.ui.ViewModel.RestaurantViewModel;
import com.cams.goforlunch.ui.ViewModel.RestaurantViewModelFactory;
import com.cams.goforlunch.ui.activities.RestaurantDetailsActivity;
import com.cams.goforlunch.ui.adapter.RestaurantItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class RestaurantListFragment extends Fragment implements RestaurantItemAdapter.RecyclerViewOnClickListener {

    public RestaurantListFragment(){
        // require a empty public constructor
    }


    private ArrayList<Restaurant> lRestaurants = new ArrayList<>();
    private RecyclerView listRestaurant;
    private RestaurantViewModel viewModel;
    private final RestaurantItemAdapter adapter = new RestaurantItemAdapter(new ArrayList<>(),this);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        listRestaurant =  requireView().findViewById(R.id.list_restaurants);
        viewModel = new ViewModelProvider(this, RestaurantViewModelFactory.getFactoryRestaurantInstance()).get(RestaurantViewModel.class);
// methode pour recup dans sharedPrefenrencies location et ensuite faire appell fetch restaurant
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String latitude = sharedPref.getString(MapsFragment.SHARED_PREF_CURRENT_LATITUDE, "");
        String longitude= sharedPref.getString(MapsFragment.SHARED_PREF_CURRENT_LONGITUDE, "");
        String location = latitude + "," + longitude;
        viewModel.fetchRestaurant(location);
        viewModel.getRestaurants().observe(getViewLifecycleOwner(), list ->{
            updateRestaurant(list);
        } );
        listRestaurant.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        listRestaurant.setAdapter(adapter);

    }

    @Override
    public void recyclerViewOnClick(int position) {
        Restaurant restaurant = lRestaurants.get(position);
        Intent intent = new Intent(getActivity(), RestaurantDetailsActivity.class);
        intent.putExtra("RESTAURANT", restaurant);
        startActivity(intent);
    }

    private void updateRestaurant(List<Restaurant> restaurants) {
        lRestaurants = (ArrayList<Restaurant>) restaurants;
        adapter.updateRestaurants(lRestaurants);

    }

}
