package com.ncr.smartkart.view.signin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ncr.smartkart.R;
import com.ncr.smartkart.utils.ActivityUtils;

public class PhoneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        setUpFragmentView();
    }

    private void setUpFragmentView() {
        PhoneNumberFragment phoneNumberFragment = (PhoneNumberFragment) getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (phoneNumberFragment == null) {
            phoneNumberFragment = PhoneNumberFragment.getInstance();
            ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(), phoneNumberFragment, R.id.frameLayout);
        }
    }
}