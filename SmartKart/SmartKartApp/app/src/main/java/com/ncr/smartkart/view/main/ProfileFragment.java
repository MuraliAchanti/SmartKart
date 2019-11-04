package com.ncr.smartkart.view.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ncr.smartkart.R;
import com.ncr.smartkart.databinding.FragmentProfileBinding;
import com.ncr.smartkart.utils.ActivityUtils;
import com.ncr.smartkart.utils.PersistentDeviceStorage;
import com.ncr.smartkart.utils.Utils;
import com.ncr.smartkart.view.signin.SignInActivity;
import com.ncr.smartkart.view.splash.SplashActivity;
import com.ncr.smartkart.viewModel.MainViewModel;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private static ProfileFragment homeFragment;
    private FragmentProfileBinding fragmentProfileBinding;
    private MainViewModel mainViewModel;
    private PersistentDeviceStorage persistentDeviceStorage;

    private String userName, profilePicURL, emailId, phoneNumber;

    public static ProfileFragment getInstance() {
        if (homeFragment == null) {
            homeFragment = new ProfileFragment();
        }
        return homeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false);
        mainViewModel = ActivityUtils.obtainViewModel(Objects.requireNonNull(getActivity()), MainViewModel.class);
        fragmentProfileBinding.setMainViewModel(mainViewModel);

        persistentDeviceStorage = PersistentDeviceStorage.getInstance(getContext());
        userName = persistentDeviceStorage.getName();
        profilePicURL = persistentDeviceStorage.getPic();
        emailId = persistentDeviceStorage.getEmail();
        phoneNumber = persistentDeviceStorage.getPhoneNumber();

        fragmentProfileBinding.logout.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.alert_dialog_style).setTitle("Logout").setMessage("This would delete all details of your account. Do you want to continue?")
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        dialogInterface.cancel();
                        persistentDeviceStorage.logout();
                        Intent intent = new Intent(getContext(), SplashActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    })
                    .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        populateView();

        return fragmentProfileBinding.getRoot();
    }

    private void populateView() {
        if (Utils.hasActiveInternetConnection(getContext()) && profilePicURL != null && (profilePicURL.contains(".com") || profilePicURL.contains(".net"))) {
            Utils.loadImage(profilePicURL, fragmentProfileBinding.profilePic, getContext());
        } else {
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(String.valueOf(userName.toCharArray()[0]).toUpperCase(), getResources().getColor(R.color.black));
            fragmentProfileBinding.profilePic.setImageDrawable(drawable);
            fragmentProfileBinding.profilePic.setScaleType(ImageView.ScaleType.FIT_XY);
            fragmentProfileBinding.profilePic.setBackground(null);
        }
        fragmentProfileBinding.profileUserName.setText(userName);
        fragmentProfileBinding.profileUserEmail.setText(emailId);
        fragmentProfileBinding.profilePhoneNumer.setText(phoneNumber);
    }
}
