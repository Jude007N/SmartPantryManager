package com.example.smartpantry.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantry.R;
import com.example.smartpantry.db.DatabaseHelper;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.model.RecipeIngredient;
import com.example.smartpantry.util.Constants;
import com.example.smartpantry.util.Fmt;

/** Screen 4: full ingredient list and method for one recipe. */
public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        long id = getIntent().getLongExtra(Constants.EXTRA_RECIPE_ID, -1);
        DatabaseHelper db = new DatabaseHelper(this);
        Recipe r = db.getRecipe(id);
        db.close();
        if (r == null) { finish(); return; }

        setTitle(r.getName());
        ((TextView) findViewById(R.id.tv_detail_name)).setText(r.getName());

        StringBuilder ing = new StringBuilder();
        for (RecipeIngredient ri : r.getIngredients()) {
            if (ing.length() > 0) ing.append('\n');
            ing.append("\u2022 ").append(Fmt.qty(ri.getQuantity()));
            ing.append("pcs".equals(ri.getUnit()) ? " x " : " " + ri.getUnit() + " ").append(ri.getName());
        }
        ((TextView) findViewById(R.id.tv_ingredients)).setText(ing);

        StringBuilder steps = new StringBuilder();
        String[] parts = r.getSteps().split("\n");
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) steps.append("\n\n");
            steps.append(i + 1).append(". ").append(parts[i]);
        }
        ((TextView) findViewById(R.id.tv_steps)).setText(steps);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
