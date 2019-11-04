package com.ncr.smartkart.view.main;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.ncr.smartkart.databinding.FragmentHistoryBinding;
import com.ncr.smartkart.models.DateItem;
import com.ncr.smartkart.models.GeneralItem;
import com.ncr.smartkart.models.Item;
import com.ncr.smartkart.models.ListItem;
import com.ncr.smartkart.models.ShoppingCart;
import com.ncr.smartkart.utils.ActivityUtils;
import com.ncr.smartkart.utils.PersistentDeviceStorage;
import com.ncr.smartkart.utils.Utils;
import com.ncr.smartkart.viewModel.MainViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HistoryFragment extends Fragment {

    private static final String TAG = "HistoryFragment";

    private static HistoryFragment homeFragment;
    private List<ListItem> listItemList = new ArrayList<>(0);

    public static HistoryFragment getInstance() {
        if (homeFragment == null) {
            homeFragment = new HistoryFragment();
        }
        return homeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentHistoryBinding fragmentHistoryBinding = FragmentHistoryBinding.inflate(inflater, container, false);
        MainViewModel mainViewModel = ActivityUtils.obtainViewModel(Objects.requireNonNull(getActivity()), MainViewModel.class);
        fragmentHistoryBinding.setMainViewModel(mainViewModel);
        PersistentDeviceStorage persistentDeviceStorage = PersistentDeviceStorage.getInstance(getContext());

        ProgressDialog progressDialog = Utils.showLoadingDialog(getContext(), false);

        FirebaseDatabase.getInstance().getReference().child("users")
                .child(persistentDeviceStorage.getPhoneNumber()).child("transactions")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<Map<String, ShoppingCart>> genericTypeIndicator = new GenericTypeIndicator<Map<String, ShoppingCart>>() {
                        };
                        for (DataSnapshot childrenSnapShot : dataSnapshot.getChildren()) {
                            listItemList.add(new DateItem(childrenSnapShot.getKey()));
                            Map<String, ShoppingCart> objectList = childrenSnapShot.getValue(genericTypeIndicator);
                            for (String key : objectList.keySet()) {
                                ShoppingCart shoppingCart = objectList.get(key);
                                listItemList.add(new GeneralItem(shoppingCart.getStoreName(), shoppingCart.getShoppingList()));
                            }
                        }
                        if (listItemList.size() != 0) {
                            HistoryAdapter historyAdapter = new HistoryAdapter(listItemList);
                            fragmentHistoryBinding.historyRecycler.setAdapter(historyAdapter);
                            fragmentHistoryBinding.historyRecycler.setVisibility(View.VISIBLE);
                            fragmentHistoryBinding.noshopping.setVisibility(View.GONE);
                        } else {
                            fragmentHistoryBinding.noshopping.setVisibility(View.VISIBLE);
                            fragmentHistoryBinding.historyRecycler.setVisibility(View.GONE);
                        }
                        progressDialog.cancel();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressDialog.cancel();
                        fragmentHistoryBinding.noshopping.setVisibility(View.VISIBLE);
                    }
                });

        return fragmentHistoryBinding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listItemList = new ArrayList<>(0);
    }
}
