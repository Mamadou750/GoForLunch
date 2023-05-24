package com.cams.goforlunch.ui.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cams.goforlunch.data.UserRepository;
import com.cams.goforlunch.model.User;

import java.util.List;

public class WorksmatesViewModel extends ViewModel {

    //REPOSITORIES
    private final UserRepository mUserRepository;

    //CONSTRUCTOR
    public WorksmatesViewModel(UserRepository mUserRepository) {
        this.mUserRepository = mUserRepository;

    }

    public LiveData<List<User>> getUsers(){
        return this.mUserRepository.getUsers();
    }

    public LiveData<User> getUser(String uid){return this.mUserRepository.getOneUser(uid);}
}
