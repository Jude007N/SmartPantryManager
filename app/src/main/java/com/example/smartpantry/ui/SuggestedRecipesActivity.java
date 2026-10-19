package com.example.smartpantry.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.db.DatabaseHelper;
import com.example.smartpantry.logic.RecipeMatcher;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.util.Constants;
import com.example.smartpantry.util.NavHelper;

import java.util.List;

/** Screen 3: runs the strict matching rule against the current pantry and lists only makeable recipes. */
public class SuggestedRecipesActivity extends AppCompatActivity implements RecipeAdapter.Listener {

    private DatabaseHelper db;
    private RecipeAdapter adapter;
    private TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);
        setTitle(R.string.title_suggested);

        db = new DatabaseHelper(this);
        emptyText = findViewById(R.id.empty_text);

        RecyclerView rv = findViewById(R.id.recipe_recycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeAdapter(this);
        rv.setAdapter(adapter);

        NavHelper.setup(this, R.id.nav_recipes);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Re-run the match each time the screen is shown so it always reflects the latest pantry
        List<Recipe> suggested = RecipeMatcher.suggest(db.getAllItems(), db.getAllRecipes());
        adapter.setRecipes(suggested);
        emptyText.setVisibility(suggested.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent i = new Intent(this, RecipeDetailActivity.class);
        i.putExtra(Constants.EXTRA_RECIPE_ID, recipe.getId());
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
