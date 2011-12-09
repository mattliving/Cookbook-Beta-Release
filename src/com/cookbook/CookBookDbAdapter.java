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
public class CookBookDbAdapter {
	// recipe table fields
    public static final String PKEY_RECIPE_ID = "_id";
	public static final String KEY_RECIPE_NAME = "recipeName";
	public static final String KEY_METHOD = "method	";
    public static final String KEY_MEAL_TYPE = "mealType";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_TIME_OF_YEAR = "timeOfYear";
    public static final String KEY_REGION = "region";
    /**
     * WE NEED 3 Other fields:
     * INGREDIENTS: a string
     * RATING: a float
     * RATING_NUMBER: a int
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

    /* TAG is used to identify this class in log messages
    private static final String TAG = "CookBookDbAdapter";*/
    
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    // sql create table strings
    private static final String RECIPE_TABLE_CREATE =
       	"create table recipe (" + PKEY_RECIPE_ID + " integer primary key " +
       	"autoincrement, " + KEY_RECIPE_NAME + " text not null, " + KEY_METHOD +
       	" text not null, " + KEY_MEAL_TYPE + " text," + KEY_DURATION +
       	" integer, " + KEY_TIME_OF_YEAR + " text, " + KEY_REGION + " text);";
    
    private static final String RECIPE_INGREDIENTS_TABLE_CREATE =
           	"create table recipeIngredients (" + PKEY_RECIPE_INGREDIENT_ID +
           	" integer primary key autoincrement, " + FKEY_RECIPE_ID +
           	" integer not null, " + FKEY_INGREDIENT_ID + " integer not null, " +
           	KEY_QUANTITY + " integer, " + KEY_UNIT + " string);";
    
    private static final String INGREDIENTS_TABLE_CREATE =
           	"create table ingredients (" + PKEY_INGREDIENT_ID +
           	" integer primary key autoincrement, " + KEY_INGREDIENT +
           	" text not null);";
    
    private static final String DATABASE_NAME = "cookbook";
    private static final String RECIPE_TABLE = "recipe";
    private static final String RECIPE_INGREDIENTS_TABLE = "recipeIngredients";
    private static final String INGREDIENTS_TABLE = "ingredients";
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
        }

        // to be completed
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
        }
    }
        
    // CookBookDbAdapter constructor takes context in which to open/create
    // the database
    public CookBookDbAdapter(Context context) {
        this.mContext = context;
    }

    // Open cookbook database (create db if not already present)
    public CookBookDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    // create a new recipe using values provided in parameters
    // call time and weight conversion here?
    public long createRecipe(String recipeName, String method, String mealType,
    	Integer duration, String timeOfYear, String region) {
    	
    	ContentValues addValues = new ContentValues();
    	addValues.put(KEY_RECIPE_NAME, recipeName);
    	addValues.put(KEY_METHOD, method);
    	addValues.put(KEY_MEAL_TYPE, mealType);
    	addValues.put(KEY_DURATION, duration);
    	addValues.put(KEY_TIME_OF_YEAR, timeOfYear);
    	addValues.put(KEY_REGION, region);

        return mDb.insert(RECIPE_TABLE, null, addValues);
    }
    
    // delete recipe corresponding to given recipe_ID
    // **this will need to be linked to deleting the corresponding
    // recipeIngredients** 
    public boolean deleteRecipe(long recipeId) {

        return mDb.delete(RECIPE_TABLE, PKEY_RECIPE_ID + "=" + recipeId, null)
        	> 0;
    }
    
    // return cursor over all recipes
    public Cursor fetchAllRecipes() {
        return mDb.query(RECIPE_TABLE, new String[] {PKEY_RECIPE_ID,
        	KEY_RECIPE_NAME, KEY_METHOD, KEY_MEAL_TYPE, KEY_DURATION,
        	KEY_TIME_OF_YEAR, KEY_REGION}, null, null, null, null, null);
    }

    // return cursor at recipe with given recipeID 
    public Cursor fetchRecipe(long recipeId) throws SQLException {

        Cursor mCursor = mDb.query(true, RECIPE_TABLE,
        	new String[] {PKEY_RECIPE_ID, KEY_RECIPE_NAME, KEY_METHOD,
        	KEY_MEAL_TYPE, KEY_DURATION, KEY_TIME_OF_YEAR, KEY_REGION},
        	PKEY_RECIPE_ID + "=" + recipeId, null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    // return cursor at recipe with given recipeName 
    public Cursor fetchRecipe(String recipeName) throws SQLException {

        Cursor mCursor = mDb.query(false, RECIPE_TABLE,
        	new String[] {PKEY_RECIPE_ID, KEY_RECIPE_NAME, KEY_METHOD,
        	KEY_MEAL_TYPE, KEY_DURATION, KEY_TIME_OF_YEAR, KEY_REGION},
        	KEY_RECIPE_NAME + "=" + recipeName, null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
   
    // Update recipe at given recipe ID with the values passed
    public boolean updateRecipe(long recipeId, String recipeName, String method,
    	String mealType, Integer duration, String timeOfYear, String region) {

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

    // create a new recipeIngredient using values provided in parameters
    public long createRecipeIngredient(Integer recipeId, Integer ingredientId,
    	Integer quantity, String unit) {
    	
    	ContentValues addValues = new ContentValues();
    	addValues.put(FKEY_RECIPE_ID, recipeId);
    	addValues.put(FKEY_INGREDIENT_ID, ingredientId);
    	addValues.put(KEY_QUANTITY, quantity);
    	addValues.put(KEY_UNIT, unit);
    	
        return mDb.insert(RECIPE_INGREDIENTS_TABLE, null, addValues);
    }

    // return cursor at recipe with given recipeID 
    public Cursor fetchRecipeIngredient(long recipeId) throws SQLException {

        Cursor mCursor = mDb.query(true, RECIPE_INGREDIENTS_TABLE,
        	new String[] {PKEY_RECIPE_INGREDIENT_ID, FKEY_RECIPE_ID,
        	FKEY_INGREDIENT_ID, KEY_QUANTITY, KEY_UNIT}, FKEY_RECIPE_ID + "=" +
        	recipeId, null, null, null, null, null);

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

        Cursor mCursor = mDb.query(true, INGREDIENTS_TABLE,
        	new String[] {PKEY_INGREDIENT_ID, KEY_INGREDIENT},
        	PKEY_INGREDIENT_ID + "=" + ingredientId,
        	null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
}
