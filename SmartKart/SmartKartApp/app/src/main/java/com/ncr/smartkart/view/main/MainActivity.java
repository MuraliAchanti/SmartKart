package com.ncr.smartkart.view.main;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.database.FirebaseDatabase;
import com.ncr.smartkart.R;
import com.ncr.smartkart.databinding.ActivityMainBinding;
import com.ncr.smartkart.models.ShoppingCart;
import com.ncr.smartkart.utils.ActivityUtils;
import com.ncr.smartkart.utils.PersistentDeviceStorage;
import com.ncr.smartkart.utils.Utils;
import com.ncr.smartkart.viewModel.MainViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    MainViewModel mainViewModel;
    ActivityMainBinding activityMainBinding;
    HomeFragment homeFragment;
    ProfileFragment profileFragment;
    HistoryFragment historyFragment;
    ShoppingFragment shoppingFragment;
    PersistentDeviceStorage persistentDeviceStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainViewModel = ActivityUtils.obtainViewModel(this, MainViewModel.class);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.setMainViewModel(mainViewModel);
        persistentDeviceStorage = PersistentDeviceStorage.getInstance(this);

        if (persistentDeviceStorage.getOngoingShopping()) {
            setUpShoppingFragment();
        } else {
            setupHomeFragment();
        }

        mainViewModel.getShowShoppingScreenEvent().observe(this, objectEvent -> {
            Boolean content = objectEvent.getContentIfNotHandled();
            if (content != null) {
                if (content) {
                    persistentDeviceStorage.setOnGoingShopping(true);
                    setUpShoppingFragment();
                } else {
                    Toast.makeText(MainActivity.this, "Error occured\nPlease try again", Toast.LENGTH_LONG).show();
                }
            }
        });

        mainViewModel.getEndShoppingEvent().observe(this, objectEvent -> {
            if (objectEvent.getContentIfNotHandled() != null) {
                persistentDeviceStorage.setOnGoingShopping(false);
                persistentDeviceStorage.removeShoppingList();
                setupHomeFragment();
            }
        });

        mainViewModel.getCheckoutCartEvent().observe(this, objectEvent -> {
            if (objectEvent.getContentIfNotHandled() != null) {
                ProgressDialog progressDialog = Utils.showLoadingDialog(MainActivity.this, false);
                // TODO : Call firebase to store data and StoreServer to store history
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String date = format.format(new Date());
                FirebaseDatabase.getInstance().getReference().child("users")
                        .child(persistentDeviceStorage.getPhoneNumber())
                        .child("transactions")
                        .child(date).push().setValue(new ShoppingCart(persistentDeviceStorage.getStoreName(), persistentDeviceStorage.getShoppingList()), (databaseError, databaseReference) -> {
                    progressDialog.cancel();
                    if (databaseError != null) {
                        Toast.makeText(MainActivity.this, "Couldn't save transaction, Please try again", Toast.LENGTH_LONG).show();
                    } else {
                        String transactionId = databaseReference.getKey();
                    }
                    mainViewModel.endShoppping();
                });
            }
        });

        activityMainBinding.bottomBar.setOnTabSelectListener(tabId -> {
            switch (tabId) {
                case R.id.tab_home:
                    if (persistentDeviceStorage.getOngoingShopping()) {
                        if (shoppingFragment != getSupportFragmentManager().findFragmentById(R.id.fragmentHolder)) {
                            if (shoppingFragment == null) {
                                shoppingFragment = ShoppingFragment.getInstance();
                            }
                            ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(), shoppingFragment, R.id.fragmentHolder);
                        }
                    } else {
                        if (homeFragment != getSupportFragmentManager().findFragmentById(R.id.fragmentHolder)) {
                            if (homeFragment == null) {
                                homeFragment = HomeFragment.getInstance();
                            }
                            ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(), homeFragment, R.id.fragmentHolder);
                        }
                    }
                    break;
                case R.id.tab_history:
                    if (historyFragment != getSupportFragmentManager().findFragmentById(R.id.fragmentHolder)) {
                        if (historyFragment == null) {
                            historyFragment = HistoryFragment.getInstance();
                        }
                        ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(), historyFragment, R.id.fragmentHolder);
                    }
                    break;
                case R.id.tab_profile:
                    if (profileFragment != getSupportFragmentManager().findFragmentById(R.id.fragmentHolder)) {
                        if (profileFragment == null) {
                            profileFragment = ProfileFragment.getInstance();
                        }
                        ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(), profileFragment, R.id.fragmentHolder);
                    }
                    break;
            }
        });
    }

    private void setUpShoppingFragment() {
        shoppingFragment = ShoppingFragment.getInstance();
        ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(), shoppingFragment, R.id.fragmentHolder);
    }

    private void setupHomeFragment() {
        homeFragment = HomeFragment.getInstance();
        ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(), homeFragment, R.id.fragmentHolder);
    }
}
