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
import com.cam.goforlunch.model.User;
import com.cam.goforlunch.ui.ViewModel.RestaurantViewModel;
import com.cam.goforlunch.ui.ViewModel.RestaurantViewModelFactory;
import com.cam.goforlunch.ui.ViewModel.UserViewModelFactory;
import com.cam.goforlunch.ui.ViewModel.WorksmatesViewModel;
import com.cam.goforlunch.ui.adapter.RestaurantItemAdapter;
import com.cam.goforlunch.ui.adapter.WorksmateItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class WorksmatesFragment extends Fragment {

    public WorksmatesFragment(){
        // require a empty public constructor
    }

    private ArrayList<User> lUsers = new ArrayList<>();
    private RecyclerView listUser;
    private WorksmatesViewModel viewModel;
    private  WorksmateItemAdapter adapter ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        viewModel = new ViewModelProvider(this, UserViewModelFactory.getFactoryUserInstance()).get(WorksmatesViewModel.class);

        return inflater.inflate(R.layout.fragment_worksmates, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        listUser =  getView().findViewById(R.id.list_tasks);
        adapter = new WorksmateItemAdapter(new ArrayList<>(),view.getContext());
        viewModel.getUsers().observe(getViewLifecycleOwner(), list ->{
            updateUser(list);
        } );
        listUser.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        listUser.setAdapter(adapter);

    }


    private void updateUser(List<User> users) {
        lUsers = (ArrayList<User>) users;

        adapter.updateUsers(lUsers);


    }
}