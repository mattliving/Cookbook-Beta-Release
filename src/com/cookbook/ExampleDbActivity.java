package com.cookbook;

import android.app.Activity;
import android.os.Bundle;

public class ExampleDbActivity extends Activity {
	private CookBookDbAdapter mDbHelper;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mDbHelper = new CookBookDbAdapter(this);
        mDbHelper.open();
        createRecipe();
        createIngredient();
        createRecipeIngredients();
    }
    
    private void createRecipe() {
    	mDbHelper.createRecipe("Spaghetti Bolgnaise", "1. Step1\n2. Step2\n" +
    		"3. Step3\n4. Step4", "Dinner", 30, "", "Italy");
    	mDbHelper.createRecipe("Mousaka", "1. StepA\n2. StepB\n" +
        		"3. StepC\n4. StepD", "Lunch", 30, "", "Greek");
    	mDbHelper.createRecipe("Cheese on Toast", "1. Pre-heat grill\n2. Slice"
        		+ "cheese\n3. Cook one side of the toast\n4. Turn toast over\n" 
    			+ "5. Put cheese on uncooked side of toast\n6. Cook until" +
    			" cheese" +	"is bubbling", "Snack", 10, "", "");
    }

    private void createRecipeIngredients() {
    	mDbHelper.createRecipeIngredient(1, 1, 6, "Qty");
    	mDbHelper.createRecipeIngredient(1, 2, 150, "g");
    	mDbHelper.createRecipeIngredient(1, 3, 300, "g");
    	mDbHelper.createRecipeIngredient(2, 1, 200, "g");
    	mDbHelper.createRecipeIngredient(2, 1, 200, "g");
    	mDbHelper.createRecipeIngredient(3, 1, 30, "g");
    	mDbHelper.createRecipeIngredient(3, 2, 3, "g");
    }

    private void createIngredient() {
    	mDbHelper.createIngredient("Tomatoes");
    	mDbHelper.createIngredient("Pasta");
    	mDbHelper.createIngredient("Beef");
    	mDbHelper.createIngredient("Lamb");
    	mDbHelper.createIngredient("Cheese");
    	mDbHelper.createIngredient("Bread");
    }
}