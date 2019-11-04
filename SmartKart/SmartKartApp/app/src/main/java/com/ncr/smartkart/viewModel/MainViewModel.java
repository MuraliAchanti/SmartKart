package com.ncr.smartkart.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ncr.smartkart.models.Item;
import com.ncr.smartkart.models.UserModel;
import com.ncr.smartkart.network.RemoteSource;
import com.ncr.smartkart.network.Repository;
import com.ncr.smartkart.utils.Event;
import com.ncr.smartkart.view.main.IItemDetails;

public class MainViewModel extends ViewModel {

    private Repository repository;
    private MutableLiveData<Event<Object>> startShoppingEvent = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> showShoppingScreenEvent = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> endShoppingEvent = new MutableLiveData<>();
    private MutableLiveData<Event<Object>> checkoutCartEvent = new MutableLiveData<>();

    public MainViewModel(Repository repository) {
        this.repository = repository;
    }

    public MutableLiveData<Event<Object>> getStartShoppingEvent() {
        return startShoppingEvent;
    }

    public MutableLiveData<Event<Boolean>> getShowShoppingScreenEvent() {
        return showShoppingScreenEvent;
    }

    public MutableLiveData<Event<Boolean>> getEndShoppingEvent() {
        return endShoppingEvent;
    }

    public MutableLiveData<Event<Object>> getCheckoutCartEvent() {
        return checkoutCartEvent;
    }

    public void startShopping() {
        startShoppingEvent.setValue(new Event<>(new Object()));
    }

    public void showShoppingScreen(UserModel userModel) {
        repository.startShopping(userModel, new RemoteSource.StartShoppingCallback() {
            @Override
            public void onDataLoaded() {
                showShoppingScreenEvent.setValue(new Event<>(true));
            }

            @Override
            public void onDataError() {
                showShoppingScreenEvent.setValue(new Event<>(false));
            }
        });
    }

    public void endShoppping() {
        endShoppingEvent.setValue(new Event<>(true));
    }

    public void checkOutCart() {
        checkoutCartEvent.setValue(new Event<>(new Object()));
    }

    public void getItemDetails(String itemId, IItemDetails iItemDetails) {
        repository.getItemDetails(itemId, new RemoteSource.GetItemDetailsCallback() {
            @Override
            public void onDataLoaded(Item item) {
                iItemDetails.getItemDetails(item);
            }

            @Override
            public void onDataError() {
                iItemDetails.getItemDetails(null);
            }
        });
    }
}
