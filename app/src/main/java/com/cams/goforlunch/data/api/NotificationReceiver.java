package com.cams.goforlunch.data.api;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;

import com.cams.goforlunch.R;
import com.cams.goforlunch.data.UserRepository;
import com.cams.goforlunch.model.Restaurant;
import com.cams.goforlunch.model.User;
import com.cams.goforlunch.ui.activities.SettingsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class NotificationReceiver extends BroadcastReceiver {

    private Restaurant restaurant;
    private String name;
    private String address;
    private ArrayList<String> workmateList;
    private String workmates;

    @Override
    public void onReceive(Context context, Intent intent) {
        getData(context);
    }

    // Get data from Firestore to display it in the notification
    private void getData(Context context) {

        workmateList = new ArrayList<>();

        UserRepository.getUser(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .addOnSuccessListener(documentSnapshot -> {

                    User currentUser = documentSnapshot.toObject(User.class);

                    if (Objects.requireNonNull(currentUser).getChosenRestaurant() != null) {
                        restaurant = currentUser.getChosenRestaurant();
                        name = currentUser.getChosenRestaurant().getName();
                        address = currentUser.getChosenRestaurant().getAddress();

                        UserRepository.getAllUsers().addOnCompleteListener(task -> {

                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                    User createUser = document.toObject(User.class);

                                    if (createUser.getChosenRestaurant() != null) {
                                        if (!createUser.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                && createUser.getChosenRestaurant().getPlaceId().equals(restaurant.getPlaceId())) {
                                            workmateList.add(createUser.getUsername());
                                        }
                                    }
                                }

                                // Create String from list of workmates
                                workmates = TextUtils.join(", ", workmateList);

                                createNotification(context);
                            }
                        });
                    }
                });

    }

    // Create the notification to display the number of results from the API request
    private void createNotification(Context context) {

        // Verify if current user has chosen a restaurant
       UserRepository.getUser(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .addOnSuccessListener(documentSnapshot -> {

                    User currentUser = documentSnapshot.toObject(User.class);
                    if (Objects.requireNonNull(currentUser).getChosenRestaurant() != null) {

                        String notificationText = String.format(context.getResources().getString(R.string.notification_alone),
                                name, address);
                        if (workmateList != null && workmateList.size() > 0)
                            notificationText = String.format(context.getResources().getString(R.string.notification_with_colleagues),
                                    name, address, workmates);

                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Intent notificationIntent = new Intent(context, SettingsActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1,
                                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        // Configure Notification Channel if API 26+
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            CharSequence name = "CHANNEL";
                            String description = "CHANNEL DESCRIPTION";
                            int importance = NotificationManager.IMPORTANCE_LOW;
                            NotificationChannel channel = new NotificationChannel("Channel", name, importance);
                            channel.setDescription(description);

                            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                            if (notificationManager != null) {
                                notificationManager.createNotificationChannel(channel);
                            }

                            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "Channel")
                                    .setSmallIcon(R.drawable.baseline_restaurant_24)
                                    .setContentTitle("Go4Lunch")
                                    .setContentText(notificationText)
                                    .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationText))
                                    .setSound(alarmSound)
                                    .setContentIntent(pendingIntent)
                                    .setAutoCancel(true);

                            if (notificationManager != null) {
                                notificationManager.notify(1, notificationBuilder.build());
                            }

                        } else {
                            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "NewsChannel")
                                    .setSmallIcon(R.drawable.baseline_restaurant_24)
                                    .setContentTitle("Go4Lunch")
                                    .setContentText(notificationText)
                                    .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationText))
                                    .setSound(alarmSound)
                                    .setContentIntent(pendingIntent)
                                    .setAutoCancel(true);
                            NotificationManager notificationManager2 = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            if (notificationManager2 != null) {
                                notificationManager2.notify(1, notificationBuilder.build());
                            }
                        }
                    }
                });
    }
}