package com.cookbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* Provides following functionality:
 * -Create/open/close database file
 * -Define table structure
 * -Create/update/delete record methods
 * -Fetch methods
 */
public class CookbookDBAdapter {
	// recipe table fields
    public static final String PKEY_RECIPE_ID = "_id";
	public static final String KEY_RECIPE_NAME = "recipeName";
	public static final String KEY_METHOD = "method	";
    public static final String KEY_MEAL_TYPE = "mealType";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_TIME_OF_YEAR = "timeOfYear";
    public static final String KEY_REGION = "region";
    public static final String KEY_RATING = "rating";
    /**
     * [comments from Giulio]
     * WE NEED 3 Other fields:
     * INGREDIENTS: a string [craig]we don't, i'll explain why
     * RATING: a float [craig]I'll add this for beta release  
     * RATING_NUMBER: a int [craig]I'll add this for beta release
     */
	
    // recipeIngredients table fields
    public static final String PKEY_RECIPE_INGREDIENT_ID = "_id";
    public static final String FKEY_RECIPE_ID = "recipeId";
    public static final String FKEY_INGREDIENT_ID = "ingredientId";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_UNIT = "unit";

    // ingredients table fields
    public static final String PKEY_INGREDIENT_ID = "_id";
    public static final String KEY_INGREDIENT = "ingredient";

    // friends table fields
    public static final String PKEY_FACEBOOK_ID = "_id";
    public static final String KEY_FIRSTNAME = "firstName";
    public static final String KEY_SURNAME = "surname";

    /* TAG is used to identify this class in log messages
    private static final String TAG = "CookBookDbAdapter";*/
    
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    // sql create table strings
    // recipe table
    private static final String RECIPE_TABLE_CREATE =
       	"create table recipe (" + PKEY_RECIPE_ID + " integer primary key " +
       	"autoincrement, " + KEY_RECIPE_NAME + " text not null, " + KEY_METHOD +
       	" text not null, " + KEY_MEAL_TYPE + " text," + KEY_DURATION +
       	" integer, " + KEY_TIME_OF_YEAR + " text, " + KEY_REGION + " text," +
       	KEY_RATING + " real);";
    
    // recipeIngredients table
    private static final String RECIPE_INGREDIENTS_TABLE_CREATE =
           	"create table recipeIngredients (" + PKEY_RECIPE_INGREDIENT_ID +
           	" integer primary key autoincrement, " + FKEY_RECIPE_ID +
           	" integer not null, " + FKEY_INGREDIENT_ID + " integer not null, " +
           	KEY_QUANTITY + " integer, " + KEY_UNIT + " string);";
    
    // ingredients table
    private static final String INGREDIENTS_TABLE_CREATE =
           	"create table ingredients (" + PKEY_INGREDIENT_ID +
           	" integer primary key autoincrement, " + KEY_INGREDIENT +
           	" text not null);";

    // friends table
    private static final String FRIENDS_TABLE_CREATE =
           	"create table friends (" + PKEY_FACEBOOK_ID +
           	" integer primary key, " + KEY_FIRSTNAME +
           	" text, " + KEY_SURNAME + " text);";

    // database and table name strings
    private static final String DATABASE_NAME = "cookbook";
    private static final String RECIPE_TABLE = "recipe";
    private static final String RECIPE_INGREDIENTS_TABLE = "recipeIngredients";
    private static final String INGREDIENTS_TABLE = "ingredients";
    private static final String FRIENDS_TABLE = "friends";
    private static final int DATABASE_VERSION = 1;
    
    private final Context mContext;

    // database creation and upgrade
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(RECIPE_TABLE_CREATE);
            db.execSQL(RECIPE_INGREDIENTS_TABLE_CREATE);
            db.execSQL(INGREDIENTS_TABLE_CREATE);
            db.execSQL(FRIENDS_TABLE_CREATE);
        }

        // to be completed
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
        }
    }
        
    // CookBookDbAdapter constructor takes context in which to open/create
    // the database
    public CookbookDBAdapter(Context context) {
        this.mContext = context;
    }

    // Open cookbook database (create db if not already present)
    public CookbookDBAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

// create a new recipe using values provided in parameters
    public long createRecipe(String recipeName, String method, String mealType,
    	int duration, String timeOfYear, String region) {
    	
    	ContentValues addValues = new ContentValues();
    	addValues.put(KEY_RECIPE_NAME, recipeName);
    	addValues.put(KEY_METHOD, method);
    	addValues.put(KEY_MEAL_TYPE, mealType);
    	addValues.put(KEY_DURATION, duration);
    	addValues.put(KEY_TIME_OF_YEAR, timeOfYear);
    	addValues.put(KEY_REGION, region);

        return mDb.insert(RECIPE_TABLE, null, addValues);
    }
	
    // create a new recipe using values provided in parameters
    public long createRecipe(String recipeName, String method, String mealType,
    	int duration, String timeOfYear, String region, int rating) {
    	
    	ContentValues addValues = new ContentValues();
    	addValues.put(KEY_RECIPE_NAME, recipeName);
    	addValues.put(KEY_METHOD, method);
    	addValues.put(KEY_MEAL_TYPE, mealType);
    	addValues.put(KEY_DURATION, duration);
    	addValues.put(KEY_TIME_OF_YEAR, timeOfYear);
    	addValues.put(KEY_REGION, region);
    	addValues.put(KEY_RATING, rating);

        return mDb.insert(RECIPE_TABLE, null, addValues);
    }
    
    // delete recipe corresponding to given recipe_ID
    // **this will need to be linked to deleting the corresponding
    // recipeIngredients** 
    public boolean deleteRecipe(long recipeId) {

        return mDb.delete(RECIPE_TABLE, PKEY_RECIPE_ID + "=" + recipeId, null)
        	> 0;
    }
    
    // return cursor over all recipes, sorted by name
    public Cursor fetchAllRecipes() {
        return mDb.query(RECIPE_TABLE, new String[] {PKEY_RECIPE_ID,
        	KEY_RECIPE_NAME, KEY_METHOD, KEY_MEAL_TYPE, KEY_DURATION,
        	KEY_TIME_OF_YEAR, KEY_REGION, KEY_RATING}, null, null, null, null, 
        	KEY_RECIPE_NAME);
    }

    // return cursor over all recipes, sorted by category then name
    public Cursor fetchAllRecipesByCategory() {
    	String mealTypeOrder = " WHEN 'Breakfast' THEN 1 WHEN 'Lunch' THEN 2 WHEN 'Dinner' THEN 3 WHEN 'Snack' THEN 4 WHEN 'Dessert' THEN 5 ELSE 99 END";
    	
        return mDb.query(RECIPE_TABLE, new String[] {PKEY_RECIPE_ID,
        	KEY_RECIPE_NAME, KEY_METHOD, KEY_MEAL_TYPE, KEY_DURATION,
        	KEY_TIME_OF_YEAR, KEY_REGION, KEY_RATING}, null, null, null, null, 
        	"CASE "+ KEY_MEAL_TYPE + mealTypeOrder + "," + KEY_RECIPE_NAME);
    }
    
    // return cursor at recipe with given recipeID 
    public Cursor fetchRecipe(long recipeId) throws SQLException {

        Cursor mCursor = mDb.query(RECIPE_TABLE,
        	new String[] {PKEY_RECIPE_ID, KEY_RECIPE_NAME, KEY_METHOD,
        	KEY_MEAL_TYPE, KEY_DURATION, KEY_TIME_OF_YEAR, KEY_REGION, KEY_RATING},
        	PKEY_RECIPE_ID + "=" + recipeId, null, null, null, KEY_RECIPE_NAME);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    // return cursor at recipe with given recipeName 
    public Cursor fetchRecipe(String recipeName) throws SQLException {

        Cursor mCursor = mDb.query(RECIPE_TABLE,
        	new String[] {PKEY_RECIPE_ID, KEY_RECIPE_NAME, KEY_METHOD,
        	KEY_MEAL_TYPE, KEY_DURATION, KEY_TIME_OF_YEAR, KEY_REGION, KEY_RATING},
        	KEY_RECIPE_NAME + "=" + "'" +recipeName+ "'", null, null, null,
        	KEY_RECIPE_NAME);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
      
    /** 
     * Search the recipes for patterns in the name
     * http://www.w3schools.com/sql/sql_like.asp
     * @param recipeName
     * @return Cursor
     * @throws SQLException
     */
    public Cursor fetchRecipeLike(String recipeName) throws SQLException {

    	Cursor mCursor = mDb.query(RECIPE_TABLE,
    		new String[] {PKEY_RECIPE_ID, KEY_RECIPE_NAME, KEY_METHOD,
    		KEY_MEAL_TYPE, KEY_DURATION, KEY_TIME_OF_YEAR, KEY_REGION, KEY_RATING},
    		KEY_RECIPE_NAME + " LIKE " + "'"+recipeName+"'", null, null, null, 
    		KEY_RECIPE_NAME);

    	if (mCursor != null) {
    		mCursor.moveToFirst();
    	}
    	return mCursor;
    }

    /**
     * Query used for suggestFromBookmarks
     * @param type
     * @param season
     * @param cookingtime
     * @return
     * @throws SQLException
     */
    public Cursor fetchRecipe(String type,String season,int cookingtime) throws SQLException {
 
        Cursor mCursor = mDb.query(RECIPE_TABLE,
                new String[] {PKEY_RECIPE_ID, KEY_RECIPE_NAME, KEY_METHOD,
                KEY_MEAL_TYPE, KEY_DURATION, KEY_TIME_OF_YEAR, KEY_REGION, KEY_RATING},
                KEY_MEAL_TYPE + "=" + "'" +type+ "'"+" AND "+KEY_TIME_OF_YEAR+"="+"'"+season+"'"+
                " AND "+KEY_DURATION+"<="+"'"+cookingtime+"'",
                        null, null, null, null);
 
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    // Update recipe at given recipe ID with the values passed
    public boolean updateRecipe(long recipeId, String recipeName, String method,
    	String mealType, int duration, String timeOfYear, String region) {

        ContentValues newValues = new ContentValues();
    	newValues.put(KEY_RECIPE_NAME, recipeName);
    	newValues.put(KEY_METHOD, method);
    	newValues.put(KEY_MEAL_TYPE, mealType);
    	newValues.put(KEY_DURATION, duration);
    	newValues.put(KEY_TIME_OF_YEAR, timeOfYear);
    	newValues.put(KEY_REGION, region);

        return mDb.update(RECIPE_TABLE, newValues,
        	PKEY_RECIPE_ID + "=" + recipeId, null) > 0;
    }

    public boolean updateRecipe(long recipeId, double rating) {
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_RATING, rating);
        
        return mDb.update(RECIPE_TABLE, newValues,
        	PKEY_RECIPE_ID + "=" + recipeId, null) > 0;
    }

    // create a new recipeIngredient using values provided in parameters
    public long createRecipeIngredient(long recipeId, long ingredientId,
    	int quantity, String unit) {
    	
    	ContentValues addValues = new ContentValues();
    	addValues.put(FKEY_RECIPE_ID, recipeId);
    	addValues.put(FKEY_INGREDIENT_ID, ingredientId);
    	addValues.put(KEY_QUANTITY, quantity);
    	addValues.put(KEY_UNIT, unit);
    	
        return mDb.insert(RECIPE_INGREDIENTS_TABLE, null, addValues);
    }

/* To be completed 
 * // create a new recipeIngredient using values provided in parameters
    public long updateRecipeIngredient(long recipeIngredientId, long recipeId, 
    	long ingredientId, int quantity, String unit) {
    	
    	ContentValues newValues = new ContentValues();
    	newValues .put(FKEY_RECIPE_ID, recipeId);
    	newValues .put(FKEY_INGREDIENT_ID, ingredientId);
    	newValues.put(KEY_QUANTITY, quantity);
    	newValues.put(KEY_UNIT, unit);
    	
        return mDb.update(RECIPE_INGREDIENTS_TABLE, newValues, 
        	PKEY_RECIPE_ID + "=" + recipeIngredientId, null);
    }*/
    
    // return cursor at recipe with given recipeID 
    public Cursor fetchRecipeIngredient(long recipeId) throws SQLException {

        Cursor mCursor = mDb.query(RECIPE_INGREDIENTS_TABLE,
        	new String[] {PKEY_RECIPE_INGREDIENT_ID, FKEY_RECIPE_ID,
        	FKEY_INGREDIENT_ID, KEY_QUANTITY, KEY_UNIT}, FKEY_RECIPE_ID + "=" +
        	recipeId, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    // create a new ingredient using ingredient name provided in parameters
    public long createIngredient(String ingredient) {
    	
    	ContentValues addValues = new ContentValues();
    	addValues.put(KEY_INGREDIENT, ingredient);
    	
        return mDb.insert(INGREDIENTS_TABLE, null, addValues);
    }

    // return cursor at ingredient with given ingredientID 
    public Cursor fetchIngredient(long ingredientId) throws SQLException {

        Cursor mCursor = mDb.query(INGREDIENTS_TABLE,
        	new String[] {PKEY_INGREDIENT_ID, KEY_INGREDIENT},
        	PKEY_INGREDIENT_ID + "=" + ingredientId,
        	null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    // return cursor at ingredient with given ingredient name 
    public Cursor fetchIngredient(String ingredientName) throws SQLException {

        Cursor mCursor = mDb.query(true, INGREDIENTS_TABLE,
        	new String[] {PKEY_INGREDIENT_ID, KEY_INGREDIENT},
        	KEY_INGREDIENT + "= '" + ingredientName + "'",
        	null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    // create friend record using facebook user id as key
    public long createFriend(long facebookId, String firstName, String surname) {
    	
    	ContentValues addValues = new ContentValues();
    	addValues.put(PKEY_FACEBOOK_ID, facebookId);
    	addValues.put(KEY_FIRSTNAME, firstName);
    	addValues.put(KEY_SURNAME, surname);
    	
        return mDb.insert(FRIENDS_TABLE, null, addValues);
    }

    public boolean updateFriend(long facebookId, String firstName, String surname) {
        ContentValues newValues = new ContentValues();
    	newValues.put(KEY_FIRSTNAME, firstName);
    	newValues.put(KEY_SURNAME, surname);
    	
    	return mDb.update(FRIENDS_TABLE, newValues,
    		PKEY_RECIPE_ID + "=" + facebookId, null) > 0;
    }
    
    public boolean deleteFriend(long facebookId) {
    	return mDb.delete(FRIENDS_TABLE, PKEY_FACEBOOK_ID + "=" + facebookId, null) > 0;
    }
    
    public boolean deleteAllFriends() {
    	return mDb.delete(FRIENDS_TABLE, null, null) > 0;
    }

    public Cursor fetchAllFriends() {
        return mDb.query(FRIENDS_TABLE, new String[] {PKEY_FACEBOOK_ID,
            KEY_FIRSTNAME, KEY_SURNAME}, null, null, null, null, null);    	
    }

}
