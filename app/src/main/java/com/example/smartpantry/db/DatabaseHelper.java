package com.example.smartpantry.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.smartpantry.data.RecipeSeed;
import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

/** SQLite storage: pantry CRUD plus the seeded recipe collection. */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "smartpantry.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE pantry (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, quantity REAL NOT NULL, unit TEXT NOT NULL, expiry TEXT)");
        db.execSQL("CREATE TABLE recipes (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, steps TEXT NOT NULL)");
        db.execSQL("CREATE TABLE recipe_ingredients (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "recipe_id INTEGER NOT NULL, name TEXT NOT NULL, quantity REAL NOT NULL, unit TEXT NOT NULL, "
                + "FOREIGN KEY(recipe_id) REFERENCES recipes(id) ON DELETE CASCADE)");
        seedRecipes(db); // runs once, the first time the database file is created
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS recipe_ingredients");
        db.execSQL("DROP TABLE IF EXISTS recipes");
        db.execSQL("DROP TABLE IF EXISTS pantry");
        onCreate(db);
    }

    private void seedRecipes(SQLiteDatabase db) {
        for (Recipe r : RecipeSeed.all()) {
            ContentValues cv = new ContentValues();
            cv.put("name", r.getName());
            cv.put("steps", r.getSteps());
            long recipeId = db.insert("recipes", null, cv);
            for (RecipeIngredient ri : r.getIngredients()) {
                ContentValues iv = new ContentValues();
                iv.put("recipe_id", recipeId);
                iv.put("name", ri.getName());
                iv.put("quantity", ri.getQuantity());
                iv.put("unit", ri.getUnit());
                db.insert("recipe_ingredients", null, iv);
            }
        }
    }

    // ---------- Pantry CRUD ----------

    private ContentValues toValues(String name, double qty, String unit, String expiry) {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("quantity", qty);
        cv.put("unit", unit);
        cv.put("expiry", expiry); // null is stored as NULL
        return cv;
    }

    /** Create */
    public long addItem(String name, double qty, String unit, String expiry) {
        return getWritableDatabase().insert("pantry", null, toValues(name, qty, unit, expiry));
    }

    /** Read all, alphabetical */
    public List<PantryItem> getAllItems() {
        List<PantryItem> list = new ArrayList<>();
        try (Cursor c = getReadableDatabase().query("pantry", null, null, null, null, null, "name COLLATE NOCASE")) {
            while (c.moveToNext()) list.add(readItem(c));
        }
        return list;
    }

    /** Read one */
    public PantryItem getItem(long id) {
        try (Cursor c = getReadableDatabase().query("pantry", null, "id=?", new String[]{String.valueOf(id)}, null, null, null)) {
            return c.moveToFirst() ? readItem(c) : null;
        }
    }

    /** Update */
    public int updateItem(long id, String name, double qty, String unit, String expiry) {
        return getWritableDatabase().update("pantry", toValues(name, qty, unit, expiry), "id=?", new String[]{String.valueOf(id)});
    }

    /** Delete */
    public int deleteItem(long id) {
        return getWritableDatabase().delete("pantry", "id=?", new String[]{String.valueOf(id)});
    }

    private PantryItem readItem(Cursor c) {
        return new PantryItem(c.getLong(c.getColumnIndexOrThrow("id")),
                c.getString(c.getColumnIndexOrThrow("name")),
                c.getDouble(c.getColumnIndexOrThrow("quantity")),
                c.getString(c.getColumnIndexOrThrow("unit")),
                c.getString(c.getColumnIndexOrThrow("expiry")));
    }

    // ---------- Recipes ----------

    public List<Recipe> getAllRecipes() {
        List<Recipe> list = new ArrayList<>();
        try (Cursor c = getReadableDatabase().query("recipes", null, null, null, null, null, "name")) {
            while (c.moveToNext()) list.add(readRecipe(c));
        }
        return list;
    }

    public Recipe getRecipe(long id) {
        try (Cursor c = getReadableDatabase().query("recipes", null, "id=?", new String[]{String.valueOf(id)}, null, null, null)) {
            return c.moveToFirst() ? readRecipe(c) : null;
        }
    }

    private Recipe readRecipe(Cursor c) {
        Recipe r = new Recipe(c.getLong(c.getColumnIndexOrThrow("id")),
                c.getString(c.getColumnIndexOrThrow("name")),
                c.getString(c.getColumnIndexOrThrow("steps")));
        try (Cursor ic = getReadableDatabase().query("recipe_ingredients", null, "recipe_id=?",
                new String[]{String.valueOf(r.getId())}, null, null, "id")) {
            while (ic.moveToNext()) {
                r.getIngredients().add(new RecipeIngredient(
                        ic.getString(ic.getColumnIndexOrThrow("name")),
                        ic.getDouble(ic.getColumnIndexOrThrow("quantity")),
                        ic.getString(ic.getColumnIndexOrThrow("unit"))));
            }
        }
        return r;
    }
}
