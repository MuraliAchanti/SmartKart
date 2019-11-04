package com.ncr.smartkart.view.signin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.ncr.smartkart.databinding.FragmentOtpBinding;
import com.ncr.smartkart.models.UserModel;
import com.ncr.smartkart.utils.ActivityUtils;
import com.ncr.smartkart.utils.OTP;
import com.ncr.smartkart.utils.PersistentDeviceStorage;
import com.ncr.smartkart.utils.Utils;
import com.ncr.smartkart.view.splash.SplashActivity;
import com.ncr.smartkart.viewModel.SignInViewModel;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created by pinna on 12/29/2017.
 */

public class OTPFragment extends Fragment {

    private static final String TAG = "OTPFragment";
    private static OTPFragment otpFragment;
    private FirebaseDatabase firebaseDatabase;
    private PersistentDeviceStorage persistentDeviceStorage;
    private CountDownTimer countDownTimer;
    private FragmentOtpBinding fragmentOtpBinding;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private String verificationId;
    private String phoneNumber;
    private OTP otp;
    private SignInViewModel signUpViewModel;
    private PhoneAuthProvider.ForceResendingToken token;

    static OTPFragment getInstance() {
        if (otpFragment == null)
            otpFragment = new OTPFragment();
        return otpFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        signUpViewModel = ActivityUtils.obtainViewModel(Objects.requireNonNull(getActivity()), SignInViewModel.class);
        phoneNumber = "+91" + getArguments().getString("phoneNumber");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentOtpBinding = FragmentOtpBinding.inflate(inflater, container, false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        fragmentOtpBinding.setSignUpViewModel(signUpViewModel);
        mAuth = FirebaseAuth.getInstance();
        persistentDeviceStorage = PersistentDeviceStorage.getInstance(getContext());
        otp = fragmentOtpBinding.inputPhone;
        otp.requestFocus();
        otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 6) {
                    if (verificationId != null) {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, s.toString());
                        // [END verify_with_code]
                        signInWithPhoneAuthCredential(credential);
                    }
                }
            }
        });

        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                fragmentOtpBinding.countDownTimer.setText(String.valueOf(millisUntilFinished / 1000));
                fragmentOtpBinding.resendCode.setVisibility(View.GONE);
            }

            @Override
            public void onFinish() {
                fragmentOtpBinding.resendCode.setVisibility(View.VISIBLE);
            }
        };

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted: in if");
                otp.setText(phoneAuthCredential.getSmsCode());
                if (phoneAuthCredential.getSmsCode() == null) {
                    Log.i(TAG, "onVerificationCompleted: sms code is null");
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }
                fragmentOtpBinding.verficationStatus.setText("Verification Success");
                countDownTimer.cancel();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.d(TAG, "onVerificationFailed: ");
                fragmentOtpBinding.verficationStatus.setText("Verification Failed");
                e.printStackTrace();
                countDownTimer.cancel();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Log.d(TAG, "onCodeSent: ");
                verificationId = s;
                fragmentOtpBinding.verficationStatus.setText("Code Sent to " + phoneNumber);
                token = forceResendingToken;
                countDownTimer.start();
            }
        };

        requestOTP(phoneNumber);

        fragmentOtpBinding.resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestOTP(phoneNumber);
            }
        });
        return fragmentOtpBinding.getRoot();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        Log.i(TAG, "signInWithPhoneAuthCredential: ");
        if (getActivity() != null) {
            mAuth.signInWithCredential(credential).addOnCompleteListener(getActivity(), task -> {
                ProgressDialog progressDialog = Utils.showLoadingDialog(getContext(), false);
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "OTP Verification success", Toast.LENGTH_LONG).show();
                    persistentDeviceStorage.setPhoneNumber(phoneNumber);
                    FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            UserModel userModel = new UserModel();
                            userModel.setEmail(persistentDeviceStorage.getEmail());
                            userModel.setName(persistentDeviceStorage.getName());
                            persistentDeviceStorage.setFcm(task1.getResult().getToken());
                            userModel.setFcm(persistentDeviceStorage.getFcm());
                            userModel.setPhotoUrl(persistentDeviceStorage.getPic());
                            firebaseDatabase.getReference().child("users").child(phoneNumber).setValue(userModel).addOnSuccessListener(aVoid -> {
                                progressDialog.cancel();
                                navigateToSplashScreen();
                            }).addOnFailureListener(e -> {
                                progressDialog.cancel();
                                persistentDeviceStorage.logout();
                                Objects.requireNonNull(getActivity()).finish();
                            });
                        } else {
                            progressDialog.cancel();
                            persistentDeviceStorage.logout();
                            Objects.requireNonNull(getActivity()).finish();
                        }
                    });
                } else {
                    progressDialog.cancel();
                    Toast.makeText(getContext(), "OTP Verification failed", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void navigateToSplashScreen() {
        Intent signUpScreenIntent = new Intent(getContext(), SplashActivity.class);
        signUpScreenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(signUpScreenIntent);
    }

    public void requestOTP(String s) {
        Log.i(TAG, "requestOTP: " + s);
        if (getActivity() != null) {
            phoneNumber = s;
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    s,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    getActivity(),               // Activity (for callback binding)
                    mCallbacks);
        }
    }
}