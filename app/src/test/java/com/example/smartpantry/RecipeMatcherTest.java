package com.example.smartpantry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.smartpantry.data.RecipeSeed;
import com.example.smartpantry.logic.IngredientMatcher;
import com.example.smartpantry.logic.RecipeMatcher;
import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.model.RecipeIngredient;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Tests the strict matching rule from section 2.3 of the brief. */
public class RecipeMatcherTest {

    private static PantryItem p(String n, double q, String u) { return new PantryItem(0, n, q, u, null); }

    private static Recipe recipe(RecipeIngredient... ing) {
        Recipe r = new Recipe(1, "Test", "step");
        r.getIngredients().addAll(Arrays.asList(ing));
        return r;
    }

    private static List<Recipe> one(Recipe r) { List<Recipe> l = new ArrayList<>(); l.add(r); return l; }

    @Test public void fiveNeededFourOwned_isExcluded() {
        Recipe r = recipe(new RecipeIngredient("a", 1, "pcs"), new RecipeIngredient("b", 1, "pcs"),
                new RecipeIngredient("c", 1, "pcs"), new RecipeIngredient("d", 1, "pcs"), new RecipeIngredient("e", 1, "pcs"));
        List<PantryItem> pantry = Arrays.asList(p("a", 1, "pcs"), p("b", 1, "pcs"), p("c", 1, "pcs"), p("d", 1, "pcs"));
        assertTrue(RecipeMatcher.suggest(pantry, one(r)).isEmpty());
        assertEquals(1, RecipeMatcher.almostThere(pantry, one(r)).size());
    }

    @Test public void allPresent_isSuggested() {
        Recipe r = recipe(new RecipeIngredient("egg", 3, "pcs"), new RecipeIngredient("butter", 10, "g"));
        List<PantryItem> pantry = Arrays.asList(p("egg", 6, "pcs"), p("butter", 250, "g"));
        assertEquals(1, RecipeMatcher.suggest(pantry, one(r)).size());
    }

    @Test public void insufficientQuantity_isExcluded() {
        Recipe r = recipe(new RecipeIngredient("egg", 3, "pcs"));
        assertTrue(RecipeMatcher.suggest(Arrays.asList(p("egg", 2, "pcs")), one(r)).isEmpty());
    }

    @Test public void pluralAndCaseDifferences_stillMatch() {
        Recipe r = recipe(new RecipeIngredient("tomato", 2, "pcs"), new RecipeIngredient("potato", 1, "pcs"));
        List<PantryItem> pantry = Arrays.asList(p("  Tomatoes ", 2, "pcs"), p("POTATOES", 1, "pcs"));
        assertEquals(1, RecipeMatcher.suggest(pantry, one(r)).size());
    }

    @Test public void unitConversion_kgCoversGrams() {
        Recipe r = recipe(new RecipeIngredient("flour", 200, "g"), new RecipeIngredient("milk", 300, "ml"));
        List<PantryItem> pantry = Arrays.asList(p("flour", 1, "kg"), p("milk", 1, "l"));
        assertEquals(1, RecipeMatcher.suggest(pantry, one(r)).size());
    }

    @Test public void tablespoonsConvertToMillilitres() {
        Recipe r = recipe(new RecipeIngredient("oil", 2, "tbsp"));
        assertEquals(1, RecipeMatcher.suggest(Arrays.asList(p("oil", 100, "ml")), one(r)).size());
        assertTrue(RecipeMatcher.suggest(Arrays.asList(p("oil", 20, "ml")), one(r)).isEmpty());
    }

    @Test public void incompatibleUnits_doNotMatch() {
        Recipe r = recipe(new RecipeIngredient("milk", 100, "ml"));
        assertTrue(RecipeMatcher.suggest(Arrays.asList(p("milk", 5, "pcs")), one(r)).isEmpty());
    }

    @Test public void duplicatePantryRows_areSummed() {
        Recipe r = recipe(new RecipeIngredient("rice", 200, "g"));
        List<PantryItem> pantry = Arrays.asList(p("rice", 100, "g"), p("rice", 150, "g"));
        assertEquals(1, RecipeMatcher.suggest(pantry, one(r)).size());
    }

    @Test public void emptyPantry_suggestsNothing() {
        assertTrue(RecipeMatcher.suggest(new ArrayList<PantryItem>(), RecipeSeed.all()).isEmpty());
    }

    @Test public void singulariser_handlesCommonCases() {
        assertEquals("tomato", IngredientMatcher.normalizeName("Tomatoes"));
        assertEquals("egg", IngredientMatcher.normalizeName("eggs"));
        assertEquals("berry", IngredientMatcher.normalizeName("berries"));
        assertEquals("olive oil", IngredientMatcher.normalizeName("Olive  Oils"));
        assertEquals("hummus", IngredientMatcher.normalizeName("hummus"));
    }

    @Test public void seedHasAtLeast15RecipesAndAllAreMakeableWithFullPantry() {
        List<Recipe> seed = RecipeSeed.all();
        assertTrue(seed.size() >= 15);
        List<PantryItem> full = new ArrayList<>();
        for (Recipe r : seed) {
            assertFalse(r.getIngredients().isEmpty());
            for (RecipeIngredient ri : r.getIngredients()) full.add(p(ri.getName(), ri.getQuantity() * 10, ri.getUnit()));
        }
        assertEquals(seed.size(), RecipeMatcher.suggest(full, seed).size());
    }

    @Test public void uppercaseUnits_stillMatch() {
        Recipe r = recipe(new RecipeIngredient("flour", 200, "g"));
        List<PantryItem> pantry = Arrays.asList(p("flour", 1, "KG"));
        assertEquals(1, RecipeMatcher.suggest(pantry, one(r)).size());
    }
}
