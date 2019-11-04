package com.ncr.smartkart.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ncr.smartkart.utils.Event;

public class SignInViewModel extends ViewModel {
    private MutableLiveData<Event<Object>> mGoogleSignInEvent = new MutableLiveData<>();
    private MutableLiveData<Event<Object>> mPhoneNumberVerifyEvent = new MutableLiveData<>();

    public void startGoogleSignIn() {
        mGoogleSignInEvent.setValue(new Event<>(new Object()));
    }

    public void startPhoneNumberVerification() {
        mPhoneNumberVerifyEvent.setValue(new Event<>(new Object()));
    }

    public MutableLiveData<Event<Object>> getGoogleSignInCommand() {
        return mGoogleSignInEvent;
    }

    public MutableLiveData<Event<Object>> getPhoneNumberVerifyCommand() {
        return mPhoneNumberVerifyEvent;
    }
}