package com.cams.goforlunch.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cams.goforlunch.R;
import com.cams.goforlunch.data.UserRepository;
import com.cams.goforlunch.model.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;


import java.util.Collections;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class LogActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        LoginManager.getInstance().logOut();
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("TAG", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("TAG", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("TAG", "facebook:onError", error);
            }
        });

    }




    @OnClick(R.id.email_button)
    public void onClickEmailLoginButton() {
         startSignInActivity(new AuthUI.IdpConfig.EmailBuilder().build()) ;
    }

    @OnClick(R.id.google_button)
    public void onClickGoogleLoginButton() {
         startSignInActivity(new AuthUI.IdpConfig.GoogleBuilder().build());
    }



    /** Login with FirebaseUI **/
    private void startSignInActivity(AuthUI.IdpConfig authUI){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Collections.singletonList(authUI))
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //mCallbackManager.onActivityResult(requestCode, resultCode, data);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // SUCCESS
            if (resultCode == RESULT_OK) {
                IdpResponse response = IdpResponse.fromResultIntent(data);
                this.handleResponseAfterSignIn(response);
                this.handleFacebookAccessToken(accessToken);
            }
        }
    }

    private void handleResponseAfterSignIn(IdpResponse response) {
        if (response != null) {
            createUserInFirestore();
            Intent intent = new Intent(LogActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Log.e("TAG", "non connecter");
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TAG", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            createUserInFirestore();
                            Intent intent = new Intent(LogActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LogActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


    //Create user in Firestore
    private void createUserInFirestore(){
        String urlPicture =(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhotoUrl() != null) ?
                Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).toString() : null;

        String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        UserRepository.getUser(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addOnSuccessListener(documentSnapshot -> {
                    User currentUser = documentSnapshot.toObject(User.class);
                    if (currentUser != null)
                        UserRepository.createUser(uid, username, urlPicture, currentUser.getChosenRestaurant(),
                                currentUser.getLikedRestaurants(), currentUser.isNotificationsEnabled());
                    else
                        UserRepository.createUser(uid, username, urlPicture, null, null, false);
                });
    }
}