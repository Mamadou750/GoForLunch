package com.cams.goforlunch.data.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cams.goforlunch.data.UserRepository;
import com.cams.goforlunch.model.User;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;

public class DeleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        UserRepository.getAllUsers().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    User createUser = document.toObject(User.class);
                    UserRepository.updateChosenRestaurant(createUser.getUserId(), null);
                }
            }
        });
    }
}
