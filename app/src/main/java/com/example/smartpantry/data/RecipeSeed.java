package com.example.smartpantry.data;

import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

/** The 20 recipes pre-loaded into the database on first run. */
public final class RecipeSeed {

    private RecipeSeed() {}

    private static RecipeIngredient i(String name, double q, String unit) {
        return new RecipeIngredient(name, q, unit);
    }

    private static Recipe r(String name, String steps, RecipeIngredient... ing) {
        Recipe rec = new Recipe(0, name, steps);
        for (RecipeIngredient x : ing) rec.getIngredients().add(x);
        return rec;
    }

    public static List<Recipe> all() {
        List<Recipe> l = new ArrayList<>();
        l.add(r("Scrambled Eggs", "Beat the eggs with salt.\nMelt butter in a pan on low heat.\nPour in eggs and stir gently until just set.",
                i("egg", 3, "pcs"), i("butter", 10, "g"), i("salt", 1, "tsp")));
        l.add(r("Cheese Omelette", "Beat the eggs.\nMelt butter in a pan and add the eggs.\nSprinkle cheese on top, fold and cook for one minute.",
                i("egg", 3, "pcs"), i("cheese", 50, "g"), i("butter", 10, "g")));
        l.add(r("Tomato Pasta", "Boil the pasta until tender.\nFry chopped onion and garlic in olive oil.\nAdd chopped tomatoes and simmer for 10 minutes.\nToss with the pasta.",
                i("pasta", 200, "g"), i("tomato", 4, "pcs"), i("onion", 1, "pcs"), i("garlic", 2, "pcs"), i("olive oil", 2, "tbsp")));
        l.add(r("Egg Fried Rice", "Cook the rice and let it cool.\nFry chopped onion in oil.\nScramble in the eggs.\nAdd rice and soy sauce and stir fry for 3 minutes.",
                i("rice", 200, "g"), i("egg", 2, "pcs"), i("onion", 1, "pcs"), i("soy sauce", 2, "tbsp"), i("oil", 1, "tbsp")));
        l.add(r("Pancakes", "Whisk flour, milk, eggs and sugar into a smooth batter.\nMelt a little butter in a pan.\nCook spoonfuls of batter until golden on both sides.",
                i("flour", 200, "g"), i("milk", 300, "ml"), i("egg", 2, "pcs"), i("sugar", 2, "tbsp"), i("butter", 20, "g")));
        l.add(r("Cheese Toastie", "Butter the bread.\nPlace cheese between the slices.\nToast in a pan until golden and melted.",
                i("bread", 4, "pcs"), i("cheese", 80, "g"), i("butter", 20, "g")));
        l.add(r("Mashed Potatoes", "Peel and boil the potatoes until soft.\nDrain and mash with butter and warm milk.\nSeason with salt.",
                i("potato", 4, "pcs"), i("butter", 30, "g"), i("milk", 100, "ml"), i("salt", 1, "tsp")));
        l.add(r("Chicken Stir Fry", "Slice the chicken, onion and peppers.\nFry chicken in oil until cooked.\nAdd vegetables and soy sauce and cook for 5 minutes.",
                i("chicken breast", 300, "g"), i("onion", 1, "pcs"), i("bell pepper", 2, "pcs"), i("soy sauce", 2, "tbsp"), i("oil", 1, "tbsp")));
        l.add(r("Banana Smoothie", "Peel the bananas.\nBlend with milk and honey until smooth.",
                i("banana", 2, "pcs"), i("milk", 250, "ml"), i("honey", 1, "tbsp")));
        l.add(r("Vegetable Soup", "Chop the carrots, potatoes and onion.\nSimmer in the stock for 25 minutes.\nSeason with salt and serve hot.",
                i("carrot", 2, "pcs"), i("potato", 2, "pcs"), i("onion", 1, "pcs"), i("vegetable stock", 500, "ml"), i("salt", 1, "tsp")));
        l.add(r("Tuna Mayo Sandwich", "Mix the tuna with mayonnaise.\nSpread on the bread and close the sandwich.",
                i("tuna", 1, "pcs"), i("mayonnaise", 2, "tbsp"), i("bread", 2, "pcs")));
        l.add(r("Oatmeal Porridge", "Heat oats and milk in a pot, stirring, for 5 minutes.\nStir in honey and serve.",
                i("oats", 80, "g"), i("milk", 300, "ml"), i("honey", 1, "tbsp")));
        l.add(r("French Toast", "Whisk eggs, milk and sugar.\nDip the bread slices.\nFry in butter until golden on both sides.",
                i("bread", 4, "pcs"), i("egg", 2, "pcs"), i("milk", 100, "ml"), i("sugar", 1, "tbsp"), i("butter", 10, "g")));
        l.add(r("Beans on Toast", "Warm the beans in a pot.\nToast and butter the bread.\nSpoon the beans on top.",
                i("baked beans", 400, "g"), i("bread", 2, "pcs"), i("butter", 10, "g")));
        l.add(r("Garlic Bread", "Mash crushed garlic into the butter.\nSpread on the bread.\nBake at 200C for 10 minutes.",
                i("bread", 4, "pcs"), i("butter", 50, "g"), i("garlic", 3, "pcs")));
        l.add(r("Lentil Curry", "Fry onion and garlic in oil.\nAdd curry powder, tomatoes and lentils with 500 ml water.\nSimmer for 25 minutes.",
                i("lentil", 200, "g"), i("onion", 1, "pcs"), i("garlic", 2, "pcs"), i("tomato", 2, "pcs"), i("curry powder", 2, "tsp"), i("oil", 1, "tbsp")));
        l.add(r("Rice Pudding", "Simmer rice in milk on low heat for 30 minutes, stirring often.\nStir in sugar and serve warm.",
                i("rice", 100, "g"), i("milk", 500, "ml"), i("sugar", 3, "tbsp")));
        l.add(r("Pap and Tomato Gravy", "Boil water and stir in the maize meal to make pap.\nFry onion in oil, add chopped tomatoes and salt.\nSimmer into a gravy and serve over the pap.",
                i("maize meal", 250, "g"), i("tomato", 3, "pcs"), i("onion", 1, "pcs"), i("oil", 1, "tbsp"), i("salt", 1, "tsp")));
        l.add(r("Chicken and Rice", "Brown the chicken with onion and garlic in oil.\nAdd rice and water and cover.\nCook until the rice is tender.",
                i("chicken breast", 300, "g"), i("rice", 200, "g"), i("onion", 1, "pcs"), i("garlic", 2, "pcs"), i("oil", 1, "tbsp")));
        l.add(r("Potato Wedges", "Cut the potatoes into wedges.\nToss with oil and salt.\nBake at 220C for 30 minutes.",
                i("potato", 4, "pcs"), i("oil", 2, "tbsp"), i("salt", 1, "tsp")));
        return l;
    }
}
