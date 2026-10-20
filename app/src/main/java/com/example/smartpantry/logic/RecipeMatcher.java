package com.example.smartpantry.logic;

import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** The strict matching rule: a recipe is suggested only if EVERY ingredient is in the pantry in enough quantity. */
public final class RecipeMatcher {

    private static final double EPS = 1e-9;

    private RecipeMatcher() {}

    /** Totals pantry quantities per (name, dimension) in base units. */
    static Map<String, Double> buildStock(List<PantryItem> pantry) {
        Map<String, Double> stock = new HashMap<>();
        for (PantryItem p : pantry) {
            String k = IngredientMatcher.key(p.getName(), p.getUnit());
            double q = IngredientMatcher.toBase(p.getQuantity(), p.getUnit());
            Double old = stock.get(k);
            stock.put(k, (old == null ? 0 : old) + q);
        }
        return stock;
    }

    // Counts how many ingredients the recipe needs that the pantry doesn't have enough of
    public static int missingCount(Recipe recipe, Map<String, Double> stock) {
        if (recipe.getIngredients().isEmpty()) return Integer.MAX_VALUE;
        // Add up repeated ingredient lines, converted to base units
        Map<String, Double> need = new HashMap<>();
        for (RecipeIngredient ri : recipe.getIngredients()) {
            String k = IngredientMatcher.key(ri.getName(), ri.getUnit());
            double q = IngredientMatcher.toBase(ri.getQuantity(), ri.getUnit());
            Double old = need.get(k);
            need.put(k, (old == null ? 0 : old) + q);
        }
        // Compare what the recipe needs with what the pantry holds
        int missing = 0;
        for (Map.Entry<String, Double> e : need.entrySet()) {
            Double have = stock.get(e.getKey());
            if (have == null || have + EPS < e.getValue()) missing++;
        }
        return missing;
    }

    /** Recipes the user can make right now (zero missing ingredients). */
    public static List<Recipe> suggest(List<PantryItem> pantry, List<Recipe> recipes) {
        return withMissing(pantry, recipes, 0);
    }

    /** Optional stretch goal: recipes missing exactly one ingredient. Keep separate from suggest(). */
    public static List<Recipe> almostThere(List<PantryItem> pantry, List<Recipe> recipes) {
        return withMissing(pantry, recipes, 1);
    }

    private static List<Recipe> withMissing(List<PantryItem> pantry, List<Recipe> recipes, int target) {
        Map<String, Double> stock = buildStock(pantry);
        List<Recipe> out = new ArrayList<>();
        for (Recipe r : recipes) {
            if (missingCount(r, stock) == target) out.add(r);
        }
        return out;
    }
}
