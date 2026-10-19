package com.example.smartpantry.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.util.Constants;
import com.example.smartpantry.util.Fmt;

import java.util.ArrayList;
import java.util.List;

/** Binds pantry rows from the database to the RecyclerView. */
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.VH> {

    public interface Listener {
        void onEdit(PantryItem item);
        void onDelete(PantryItem item);
    }

    private final List<PantryItem> items = new ArrayList<>();
    private final Listener listener;
    private boolean highlightExpiring;

    public PantryAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setItems(List<PantryItem> newItems, boolean highlightExpiring) {
        items.clear();
        items.addAll(newItems);
        this.highlightExpiring = highlightExpiring;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        PantryItem item = items.get(position);
        h.name.setText(item.getName());
        h.qty.setText(Fmt.qty(item.getQuantity()) + " " + item.getUnit());

        Long days = Fmt.daysUntil(item.getExpiry());
        if (days == null) {
            h.expiry.setText(R.string.no_expiry);
            h.expiry.setTextColor(Color.GRAY);
        } else if (highlightExpiring && days <= Constants.EXPIRY_SOON_DAYS) {
            h.expiry.setText(days < 0 ? "Expired " + item.getExpiry() : "Expires soon: " + item.getExpiry());
            h.expiry.setTextColor(Color.RED);
        } else {
            h.expiry.setText("Expires " + item.getExpiry());
            h.expiry.setTextColor(Color.GRAY);
        }

        h.itemView.setOnClickListener(v -> listener.onEdit(item));
        h.delete.setOnClickListener(v -> listener.onDelete(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final TextView name, qty, expiry, delete;

        VH(View v) {
            super(v);
            name = v.findViewById(R.id.tv_name);
            qty = v.findViewById(R.id.tv_qty);
            expiry = v.findViewById(R.id.tv_expiry);
            delete = v.findViewById(R.id.btn_delete);
        }
    }
}
