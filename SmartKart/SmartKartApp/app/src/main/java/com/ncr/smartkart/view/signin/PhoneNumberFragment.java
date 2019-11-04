package com.ncr.smartkart.view.signin;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.ncr.smartkart.R;
import com.ncr.smartkart.databinding.FragmentPhoneNumberBinding;
import com.ncr.smartkart.utils.ActivityUtils;
import com.ncr.smartkart.utils.Event;
import com.ncr.smartkart.utils.Utils;
import com.ncr.smartkart.viewModel.SignInViewModel;

import java.util.Objects;

public class PhoneNumberFragment extends Fragment {


    private static final String TAG = "PhoneNumberFragment";
    private FragmentPhoneNumberBinding fragmentPhoneNumberBinding;
    private SignInViewModel signInViewModel;

    static PhoneNumberFragment getInstance() {
        return new PhoneNumberFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentPhoneNumberBinding = FragmentPhoneNumberBinding.inflate(inflater, container, false);
        signInViewModel = ActivityUtils.obtainViewModel(Objects.requireNonNull(getActivity()), SignInViewModel.class);
        fragmentPhoneNumberBinding.setSignInViewModel(signInViewModel);

        signInViewModel.getPhoneNumberVerifyCommand().observe(this, new Observer<Event<Object>>() {
            @Override
            public void onChanged(Event<Object> objectEvent) {
                setUpOTPFragment();
            }
        });

        fragmentPhoneNumberBinding.next.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_GO) {
                    signInViewModel.startPhoneNumberVerification();
                    return true;
                }
                return false;
            }
        });

        return fragmentPhoneNumberBinding.getRoot();
    }

    private void setUpOTPFragment() {
        String phoneNumber = fragmentPhoneNumberBinding.phoneNumberEditText.getText().toString();
        if (phoneNumber.length() == 10) {
            if (Utils.hasActiveInternetConnection(Objects.requireNonNull(getContext()))) {
                OTPFragment otpFragment = OTPFragment.getInstance();
                Bundle otpBundle = new Bundle();
                Log.i(TAG, "setUpOTPFragment: phone number " + phoneNumber);
                otpBundle.putString("phoneNumber", phoneNumber);
                otpFragment.setArguments(otpBundle);
                if (getFragmentManager() != null) {
                    ActivityUtils.replaceFragmentInActivity(getFragmentManager(), otpFragment, R.id.frameLayout);
                }
            } else {
                Toast.makeText(getContext(), "No Internet, please try again", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Enter valid phone number", Toast.LENGTH_LONG).show();
        }
    }
}