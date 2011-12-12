package com.cookbook;

import com.cookbook.core.RecipeList;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

public class CookbookActivity extends Activity {
	// Create new Facebook object with APP_ID
	Facebook facebook = new Facebook("263632013686454");
	// DB Adapter
	private CookBookDbAdapter mDbHelper;	
	// List of recipes
	RecipeList list = new RecipeList();

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        facebook.authorize(this, new DialogListener() {
            public void onComplete(Bundle values) {}

            public void onFacebookError(FacebookError error) {}
            public void onError(DialogError e) {}
            public void onCancel() {}
        });
        
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(CookbookActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
        
        // Initialize the DB
        mDbHelper = new CookBookDbAdapter(this);
        mDbHelper.open();
        createRecipe();
        createIngredient();
        createRecipeIngredients();
        
        /*
         * Add the database entries to the list
         */
        list.fetchAllRecipes(mDbHelper);
        
        /**
         * Debugging messages in Android
         */
        Log.d("MyDebug", String.valueOf(list.size()));
        
        /**
        * Adding the list to the recipeArray, which is used to display it
		*/
		RECIPES = new String[list.size()];
		for (int i =0; i<list.size();i++){
			RECIPES[i] = list.getRecipe(i).getName()+"\nType: "+list.getRecipe(i).getType();
			System.out.println(list.getRecipe(i).getName());
  	  	}
        
		/* list_item is in /res/layout/ should be created
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, RECIPES));
		
		final ListView lv = getListView();
		lv.setTextFilterEnabled(true);*/
		
		/**
		* On click, show the info about the recipe in a pop-up message
		*/
		/*lv.setOnItemClickListener(new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view,
			int position, long id) {
				// When clicked, show a toast with the TextView text
				Toast.makeText(getApplicationContext(), 
				"Ingredients: "+list.getRecipe(position).getIngredients()+"\nPreparation: "+list.getRecipe(position).getPreparation()
				+"\nType: "+list.getRecipe(position).getType()+"\nRegion: "+list.getRecipe(position).getRegion(),
				Toast.LENGTH_SHORT).show();
			}
		});*/
	}
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        facebook.authorizeCallback(requestCode, resultCode, data);
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
    
    /** Need it*/
	String[] RECIPES = new String[]{"lol"}; 
}