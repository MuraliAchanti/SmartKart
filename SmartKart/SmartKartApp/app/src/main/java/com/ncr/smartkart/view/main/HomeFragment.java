package com.ncr.smartkart.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.ncr.smartkart.SmartKart;
import com.ncr.smartkart.databinding.FragmentHomeBinding;
import com.ncr.smartkart.models.UserModel;
import com.ncr.smartkart.utils.ActivityUtils;
import com.ncr.smartkart.utils.PersistentDeviceStorage;
import com.ncr.smartkart.viewModel.MainViewModel;

import java.net.URL;
import java.util.Map;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private static HomeFragment homeFragment;
    private PersistentDeviceStorage persistentDeviceStorage;
    private MainViewModel mainViewModel;

    public static HomeFragment getInstance() {
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        return homeFragment;
    }

    public static boolean isValid(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentHomeBinding fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        mainViewModel = ActivityUtils.obtainViewModel(Objects.requireNonNull(getActivity()), MainViewModel.class);
        fragmentHomeBinding.setMainViewModel(mainViewModel);
        persistentDeviceStorage = PersistentDeviceStorage.getInstance(getContext());

        mainViewModel.getStartShoppingEvent().observe(this, (objectEvent) -> {
            if (objectEvent.getContentIfNotHandled() != null) {
                IntentIntegrator.forSupportFragment(HomeFragment.this).initiateScan();
            }
        });

        return fragmentHomeBinding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getContext(), "Please scan a valid QR", Toast.LENGTH_LONG).show();
            } else {
                Map<String, String> storeMap = new Gson().fromJson(result.getContents(), new TypeToken<Map<String, String>>() {
                }.getType());
                String storeUrl = storeMap.get("url");
                String storeName = storeMap.get("storeName");
                if (isValid(storeUrl)) {
                    persistentDeviceStorage.setStoreUrl(storeUrl);
                    persistentDeviceStorage.setStoreName(storeName);
                    SmartKart.getInstance().networkContextInit();

                    UserModel userModel = new UserModel();
                    userModel.setName(persistentDeviceStorage.getName());
                    userModel.setPhotoUrl(persistentDeviceStorage.getPic());
                    userModel.setFcm(persistentDeviceStorage.getFcm());
                    userModel.setEmail(persistentDeviceStorage.getEmail());
                    userModel.setPhoneNumber(persistentDeviceStorage.getPhoneNumber());

                    mainViewModel.showShoppingScreen(userModel);
                } else {
                    Toast.makeText(getContext(), "Not a valid store URL", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
