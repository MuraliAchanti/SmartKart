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

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.ncr.smartkart.databinding.FragmentShoppingBinding;
import com.ncr.smartkart.models.Item;
import com.ncr.smartkart.utils.ActivityUtils;
import com.ncr.smartkart.utils.PersistentDeviceStorage;
import com.ncr.smartkart.viewModel.MainViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShoppingFragment extends Fragment {

    private static ShoppingFragment homeFragment;
    MainViewModel mainViewModel;
    private List<Item> itemList;
    private FragmentShoppingBinding fragmentShoppingBinding;
    private ItemsAdapter itemsAdapter;

    public static ShoppingFragment getInstance() {
        if (homeFragment == null) {
            homeFragment = new ShoppingFragment();
        }
        return homeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentShoppingBinding = FragmentShoppingBinding.inflate(inflater, container, false);
        mainViewModel = ActivityUtils.obtainViewModel(Objects.requireNonNull(getActivity()), MainViewModel.class);
        fragmentShoppingBinding.setMainViewModel(mainViewModel);
        PersistentDeviceStorage persistentDeviceStorage = PersistentDeviceStorage.getInstance(getContext());
        itemList = persistentDeviceStorage.getShoppingList();
        if (itemList == null) {
            itemList = new ArrayList<>(0);
        }
        itemsAdapter = new ItemsAdapter(itemList, mainViewModel, persistentDeviceStorage);
        fragmentShoppingBinding.itemsRecycler.setAdapter(itemsAdapter);

        fragmentShoppingBinding.addNewItem.setOnClickListener(view -> IntentIntegrator.forSupportFragment(ShoppingFragment.this).initiateScan());

        return fragmentShoppingBinding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getContext(), "Please scan a valid QR", Toast.LENGTH_LONG).show();
            } else {
                String itemId = result.getContents();
                boolean found = false;
                int cnt = 0;
                for (Item item : itemList) {
                    if (item.getItemId().equals(itemId)) {
                        found = true;
                        break;
                    }
                    cnt++;
                }
                if (!found) {
                    mainViewModel.getItemDetails(itemId, item -> {
                        if (item == null) {
                            Toast.makeText(getContext(), "Couldn't find item", Toast.LENGTH_LONG).show();
                        } else {
                            item.setQuantity(1);
                            itemsAdapter.addItem(item);
                        }
                    });
                } else {
                    fragmentShoppingBinding.itemsRecycler.scrollToPosition(cnt);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
