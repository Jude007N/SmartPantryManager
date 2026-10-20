# Smart Pantry Manager

Smart Pantry Manager is an Android app I built in Java for Mobile App Development 700. Food often gets thrown away simply because people forget what they have at home, so the app lets you keep a list of the ingredients in your pantry and then shows only the recipes you can cook right now with exactly what you already own. If a recipe needs even one ingredient you don't have, it isn't suggested.

## Features
- Pantry management: add, edit and delete ingredients (name, quantity, unit, optional expiry date), with input validation
- Pantry list screen (RecyclerView with a custom adapter bound to the database)
- 20 pre-loaded recipes (seeded on first run)
- Suggested Recipes screen using the strict matching rule: a recipe appears only if every ingredient is in the pantry in at least the required quantity
- Matching copes with plural/singular names ("tomato" vs "tomatoes") and unit differences ("1 kg" vs "200 g", "tbsp" vs "ml")
- Recipe detail screen, plus a Settings screen (highlight items expiring within 3 days)
- Friendly message when no recipes match

## Database choice: SQLite (SQLiteOpenHelper)
I chose SQLite for the database. The app only stores one person's pantry, so it doesn't need an online server or a user account, and SQLite stores everything on the phone itself. That means the app works without internet, my data is still there after I close and reopen it, and it covers all the create, read, update and delete operations the assignment requires. I used SQLiteOpenHelper because it is the approach we covered in the persistent data chapter of the module.

## Setup and run
I built and tested this app in Android Studio Rabbit 1 using the Medium Phone emulator on API 37. To run it, open the project folder in Android Studio, wait for the Gradle sync to finish, choose an emulator or a connected phone, and press Run. The recipes are added to the database automatically the first time the app starts.

## Project structure
- `model` : PantryItem, Recipe, RecipeIngredient
- `logic` : IngredientMatcher (name/unit normalisation) and RecipeMatcher (strict matching rule)
- `data` : RecipeSeed (the 20 pre-loaded recipes)
- `db` : DatabaseHelper (SQLite CRUD)
- `ui` : the five Activities and the two RecyclerView adapters

## Testing
The matching logic is covered by JUnit tests in app/src/test (RecipeMatcherTest). They check the strict matching rule, singular and plural names, unit conversion, upper case units, insufficient quantities and summing duplicate pantry rows. To run them, right-click the test folder in Android Studio and choose Run Tests.