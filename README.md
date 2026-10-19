# Smart Pantry Manager

A Java Android app (Mobile App Development 700) that helps reduce food waste. You record the ingredients you actually have at home and the app suggests recipes you can cook using strictly those ingredients, with no shopping trip needed.

## Features
- Pantry management: add, edit and delete ingredients (name, quantity, unit, optional expiry date), with input validation
- Pantry list screen (RecyclerView with a custom adapter bound to the database)
- 20 pre-loaded recipes (seeded on first run)
- Suggested Recipes screen using the strict matching rule: a recipe appears only if every ingredient is in the pantry in at least the required quantity
- Matching copes with plural/singular names ("tomato" vs "tomatoes") and unit differences ("1 kg" vs "200 g", "tbsp" vs "ml")
- Recipe detail screen, plus a Settings screen (highlight items expiring within 3 days)
- Friendly message when no recipes match

## Database choice: SQLite (SQLiteOpenHelper)
SQLite runs on the device, needs no account, server or internet, and persists data between launches. That suits a personal pantry app, and it keeps the project simple to set up and run. Tables: `pantry`, `recipes`, `recipe_ingredients`.

## Setup and run
1. Install Android Studio (Koala or newer) with an Android SDK (API 34).
2. Open Android Studio, choose File > Open and select this folder. Let Gradle sync finish.
3. Create or choose an emulator (API 24 or higher), or connect a physical device with USB debugging on.
4. Press Run. The app opens on the Pantry screen.
5. To run the unit tests for the matching logic: right-click `app/src/test` and choose Run Tests.

## Project structure
- `model` : PantryItem, Recipe, RecipeIngredient
- `logic` : IngredientMatcher (name/unit normalisation) and RecipeMatcher (strict matching rule)
- `data` : RecipeSeed (the 20 pre-loaded recipes)
- `db` : DatabaseHelper (SQLite CRUD)
- `ui` : the five Activities and the two RecyclerView adapters
