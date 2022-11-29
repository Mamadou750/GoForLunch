package com.cam.goforlunch.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cam.goforlunch.R;
import com.cam.goforlunch.model.Restaurant;
import com.cam.goforlunch.ui.ViewModel.RestaurantViewModel;
import com.cam.goforlunch.ui.ViewModel.RestaurantViewModelFactory;
import com.cam.goforlunch.ui.adapter.RestaurantItemAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        viewModel.getRestaurants().observe(getViewLifecycleOwner(), list ->{
            updateRestaurant(list);
        } );
        listRestaurant.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        listRestaurant.setAdapter(adapter);

    }

    @Override
    public void recyclerViewOnClick(int position) {

    }

    private void updateRestaurant(List<Restaurant> restaurants) {
        lRestaurants = (ArrayList<Restaurant>) restaurants;

        adapter.updateRestaurants(lRestaurants);


    }


}
