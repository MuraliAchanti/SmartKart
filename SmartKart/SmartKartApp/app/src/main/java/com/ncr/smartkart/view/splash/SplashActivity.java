package com.ncr.smartkart.view.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.ncr.smartkart.R;
import com.ncr.smartkart.utils.PersistentDeviceStorage;
import com.ncr.smartkart.view.main.MainActivity;
import com.ncr.smartkart.view.signin.PhoneActivity;
import com.ncr.smartkart.view.signin.SignInActivity;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    FirebaseAuth firebaseAuth;
    PersistentDeviceStorage persistentDeviceStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        persistentDeviceStorage = PersistentDeviceStorage.getInstance(this);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkSignUp();
    }

    private void checkSignUp() {
        new Handler().postDelayed(() -> {
            if (!"null".equalsIgnoreCase(persistentDeviceStorage.getEmail())) {
                if ("null".equalsIgnoreCase(persistentDeviceStorage.getPhoneNumber())) {
                    navigateToPhoneSignIn();
                } else {
                    navigateToHomeScreen();
                }
            } else {
                navigateToSignIn();
            }
        }, 800);
    }

    private void navigateToSignIn() {
        Intent intent = new Intent(this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void navigateToHomeScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void navigateToPhoneSignIn() {
        Intent intent = new Intent(this, PhoneActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}