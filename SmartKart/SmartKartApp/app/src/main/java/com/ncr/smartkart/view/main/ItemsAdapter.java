package com.ncr.smartkart.view.main;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ncr.smartkart.databinding.ItemLayoutBinding;
import com.ncr.smartkart.models.Item;
import com.ncr.smartkart.utils.PersistentDeviceStorage;
import com.ncr.smartkart.utils.Utils;
import com.ncr.smartkart.viewModel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    private List<Item> itemList;
    private MainViewModel mainViewModel;
    private PersistentDeviceStorage persistentDeviceStorage;

    ItemsAdapter(List<Item> itemList, MainViewModel mainViewModel, PersistentDeviceStorage persistentDeviceStorage) {
        this.itemList = itemList;
        this.mainViewModel = mainViewModel;
        this.persistentDeviceStorage = persistentDeviceStorage;
    }

    void addItem(Item item) {
        if (itemList == null) {
            itemList = new ArrayList<>(0);
        }
        itemList.add(item);
        persistShoppingList();
        notifyDataSetChanged();
    }

    private void persistShoppingList() {
        persistentDeviceStorage.setShoppingList(itemList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemLayoutBinding itemLayoutBinding = ItemLayoutBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return (itemList == null) ? 0 : itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemLayoutBinding itemLayoutBinding;

        ViewHolder(ItemLayoutBinding itemLayoutBinding) {
            super(itemLayoutBinding.getRoot());
            this.itemLayoutBinding = itemLayoutBinding;
        }

        public void bind(Item item) {
            itemLayoutBinding.setMainViewModel(mainViewModel);
            itemLayoutBinding.setItem(item);
            itemLayoutBinding.executePendingBindings();

            itemLayoutBinding.decrement.setOnClickListener(view -> {
                int position = getAdapterPosition();
                int value = itemList.get(position).getQuantity();
                value--;
                if (value == 0) {
                    itemList.remove(position);
                } else {
                    itemList.get(position).setQuantity(value);
                }
                persistShoppingList();
                notifyDataSetChanged();
            });

            itemLayoutBinding.increment.setOnClickListener(view -> {
                int position = getAdapterPosition();
                itemList.get(position).setQuantity(itemList.get(position).getQuantity() + 1);
                persistShoppingList();
                notifyDataSetChanged();
            });
        }
    }
}
