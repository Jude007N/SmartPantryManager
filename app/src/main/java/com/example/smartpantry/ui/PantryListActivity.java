package com.example.smartpantry.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.db.DatabaseHelper;
import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.util.Constants;
import com.example.smartpantry.util.NavHelper;

import java.util.List;

/** Screen 1: the pantry list (Read + entry point for Create, Update, Delete). */
public class PantryListActivity extends AppCompatActivity implements PantryAdapter.Listener {

    private DatabaseHelper db;
    private PantryAdapter adapter;
    private TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry_list);
        setTitle(R.string.title_pantry);

        db = new DatabaseHelper(this);
        emptyText = findViewById(R.id.empty_text);

        RecyclerView rv = findViewById(R.id.pantry_recycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PantryAdapter(this);
        rv.setAdapter(adapter);

        // Explicit Intent to the Add/Edit screen (no extra = create mode)
        findViewById(R.id.fab_add).setOnClickListener(v ->
                startActivity(new Intent(this, AddEditItemActivity.class)));

        NavHelper.setup(this, R.id.nav_pantry);
    }

    // Reload every time we come back, so edits made on other screens show up
    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        List<PantryItem> items = db.getAllItems();
        boolean alerts = getSharedPreferences(Constants.PREFS, MODE_PRIVATE).getBoolean(Constants.KEY_EXPIRY_ALERTS, true);
        adapter.setItems(items, alerts);
        emptyText.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onEdit(PantryItem item) {
        Intent i = new Intent(this, AddEditItemActivity.class);
        i.putExtra(Constants.EXTRA_ITEM_ID, item.getId()); // pass the row id so the next screen can load it
        startActivity(i);
    }

    @Override
    public void onDelete(PantryItem item) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_title)
                .setMessage("Remove " + item.getName() + " from your pantry?")
                .setPositiveButton(R.string.delete, (d, w) -> {
                    db.deleteItem(item.getId());
                    refresh();
                    Toast.makeText(this, R.string.item_deleted, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
