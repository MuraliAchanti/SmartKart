package com.ncr.smartkart.view.main;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ncr.smartkart.R;
import com.ncr.smartkart.models.DateItem;
import com.ncr.smartkart.models.GeneralItem;
import com.ncr.smartkart.models.Item;
import com.ncr.smartkart.models.ListItem;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "HistoryAdapter";
    private List<ListItem> consolidatedList = new ArrayList<>();

    public HistoryAdapter(List<ListItem> consolidatedList) {
        this.consolidatedList = consolidatedList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType) {
            case ListItem.TYPE_DATE:
                view = inflater.inflate(R.layout.date_layout, parent, false);
                viewHolder = new DateViewHolder(view);
                break;
            case ListItem.TYPE_GENERAL:
                view = inflater.inflate(R.layout.general_layout, parent, false);
                viewHolder = new GeneralViewHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case ListItem.TYPE_DATE:
                DateItem dateItem = (DateItem) consolidatedList.get(position);
                DateViewHolder dateViewHolder = (DateViewHolder) holder;
                dateViewHolder.dateTextView.setText(dateItem.getDate());
                break;
            case ListItem.TYPE_GENERAL:
                GeneralItem generalItem = (GeneralItem) consolidatedList.get(position);
                GeneralViewHolder generalViewHolder = (GeneralViewHolder) holder;
                String storeName = generalItem.getStoreName();
                List<Item> itemList = generalItem.getItemList();
                Log.i(TAG, "onBindViewHolder: " + itemList.size());
                Float price = 0.0f, weight = 0.0f;
                for (Item item : itemList) {
                    price += item.getPrice() * item.getQuantity();
                    weight += item.getWeight() * item.getQuantity();
                }
                generalViewHolder.storeNameTextView.setText("Store Name : " + storeName);
                generalViewHolder.priceTextView.setText("Price : " + price);
                generalViewHolder.weightTextView.setText("Weight : " + weight);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return consolidatedList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return consolidatedList == null ? 0 : consolidatedList.size();
    }

    class DateViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.date_text);
        }
    }

    class GeneralViewHolder extends RecyclerView.ViewHolder {
        TextView priceTextView, weightTextView, storeNameTextView;

        public GeneralViewHolder(@NonNull View itemView) {
            super(itemView);
            storeNameTextView = itemView.findViewById(R.id.store_name);
            priceTextView = itemView.findViewById(R.id.general_price);
            weightTextView = itemView.findViewById(R.id.general_weight);
        }
    }
}
