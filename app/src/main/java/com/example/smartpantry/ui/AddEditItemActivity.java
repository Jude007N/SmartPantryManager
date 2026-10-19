package com.example.smartpantry.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantry.R;
import com.example.smartpantry.db.DatabaseHelper;
import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.util.Constants;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

/** Screen 2: add a new ingredient, or edit an existing one when an item id is passed in the Intent. */
public class AddEditItemActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private long itemId = -1;
    private TextInputLayout nameLayout, qtyLayout;
    private TextInputEditText nameEdit, qtyEdit, expiryEdit;
    private Spinner unitSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_item);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);
        nameLayout = findViewById(R.id.til_name);
        qtyLayout = findViewById(R.id.til_qty);
        nameEdit = findViewById(R.id.et_name);
        qtyEdit = findViewById(R.id.et_qty);
        expiryEdit = findViewById(R.id.et_expiry);
        unitSpinner = findViewById(R.id.spinner_unit);

        unitSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Constants.UNITS));

        // Read the Intent extra: -1 means "create", anything else means "edit this row"
        itemId = getIntent().getLongExtra(Constants.EXTRA_ITEM_ID, -1);
        if (itemId != -1) {
            setTitle(R.string.title_edit);
            PantryItem item = db.getItem(itemId);
            if (item == null) { finish(); return; }
            nameEdit.setText(item.getName());
            qtyEdit.setText(com.example.smartpantry.util.Fmt.qty(item.getQuantity()));
            int idx = Arrays.asList(Constants.UNITS).indexOf(item.getUnit());
            unitSpinner.setSelection(Math.max(idx, 0));
            if (item.getExpiry() != null) expiryEdit.setText(item.getExpiry());
        } else {
            setTitle(R.string.title_add);
        }

        expiryEdit.setOnClickListener(v -> showDatePicker());
        findViewById(R.id.btn_clear_date).setOnClickListener(v -> expiryEdit.setText(""));
        findViewById(R.id.btn_save).setOnClickListener(v -> save());
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, y, m, d) ->
                expiryEdit.setText(String.format(Locale.US, "%04d-%02d-%02d", y, m + 1, d)),
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    /** Validates the form, then inserts or updates. Shows inline errors and stops on the first failure. */
    private void save() {
        nameLayout.setError(null);
        qtyLayout.setError(null);

        String name = nameEdit.getText() == null ? "" : nameEdit.getText().toString().trim();
        String qtyText = qtyEdit.getText() == null ? "" : qtyEdit.getText().toString().trim();

        if (name.isEmpty()) { nameLayout.setError(getString(R.string.err_name_required)); return; }
        if (name.length() > 40) { nameLayout.setError(getString(R.string.err_name_long)); return; }
        if (!name.matches("[\\p{L}][\\p{L} '\\-]*")) { nameLayout.setError(getString(R.string.err_name_chars)); return; }

        double qty;
        try {
            qty = Double.parseDouble(qtyText);
        } catch (NumberFormatException e) {
            qtyLayout.setError(getString(R.string.err_qty_invalid));
            return;
        }
        if (qty <= 0 || qty > 100000) { qtyLayout.setError(getString(R.string.err_qty_range)); return; }

        String unit = (String) unitSpinner.getSelectedItem();
        String expiry = expiryEdit.getText() == null ? "" : expiryEdit.getText().toString().trim();
        if (expiry.isEmpty()) expiry = null; // expiry is optional

        if (itemId == -1) db.addItem(name, qty, unit, expiry);
        else db.updateItem(itemId, name, qty, unit, expiry);

        Toast.makeText(this, R.string.item_saved, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
