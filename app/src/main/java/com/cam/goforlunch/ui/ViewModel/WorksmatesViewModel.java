package com.cam.goforlunch.ui.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cam.goforlunch.data.RestaurantRepository;
import com.cam.goforlunch.data.UserRepository;
import com.cam.goforlunch.model.Restaurant;
import com.cam.goforlunch.model.User;

import java.util.List;

public class WorksmatesViewModel extends ViewModel {

    //REPOSITORIES
    private final UserRepository mUserRepository;

    //CONSTRUCTOR
    public WorksmatesViewModel(UserRepository mUserRepository) {
        this.mUserRepository = mUserRepository;

    }

    public LiveData<List<User>> getUsers(){
        return mUserRepository.getUsers();
    }
}
