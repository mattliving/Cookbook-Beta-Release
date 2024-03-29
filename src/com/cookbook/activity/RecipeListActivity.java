package com.cookbook.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.cookbook.R;
import com.cookbook.RecipeList;
import com.cookbook.adapter.CookbookDBAdapter;

public class RecipeListActivity extends ListActivity {
    
	/**
	 * Database Adapter
	 */
	protected CookbookDBAdapter mDbHelper;
	
	/** List of Recipes */
	protected RecipeList list = new RecipeList();
	
	/** ListView */
	protected  ListView lv;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
        //Initialise the DB
        mDbHelper = new CookbookDBAdapter(this);
        mDbHelper.open();
        list.fetchAllRecipes(mDbHelper);
        
        Log.d("MyDebug", String.valueOf(list.size()));
        
        /*ImageButton homeButton = (ImageButton) findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(v.getContext(), CookbookActivity.class);
            	startActivity(intent);
            }
        });
        
        ImageButton searchButton = (ImageButton) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(v.getContext(), SearchActivity.class);
            	startActivity(intent);
            }
        });*/
        
		RECIPES = new String[list.size()];
		for (int i =0; i<list.size();i++){
			RECIPES[i] = list.getRecipe(i).getName()+"\nType: "+list.getRecipe(i).getType();
			System.out.println(list.getRecipe(i).getName());
		} 
        
		// list_item is in /res/layout/ should be created
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, RECIPES));
		
		lv = getListView();
		lv.setTextFilterEnabled(true);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view,
			int position, long id) {
				Intent recIntent = new Intent(view.getContext(),ViewRecipeActivity.class);
				// trying to send the recipe name to the new activity
				recIntent.putExtra("recipeName",list.getRecipe(position).getName());
				startActivity(recIntent);
				    	
			}
		});
	}
    
    private void createRecipe() {
    	/*mDbHelper.createRecipe("Spaghetti Bolgnaise", "1. Step1\n2. Step2\n" +
    		"3. Step3\n4. Step4", "Dinner", 30, "", "Italy");
    	mDbHelper.createRecipe("Mousaka", "1. StepA\n2. StepB\n" +
        		"3. StepC\n4. StepD", "Lunch", 30, "", "Greek");
    	mDbHelper.createRecipe("Cheese on Toast", "1. Pre-heat grill\n2. Slice"
        		+ "cheese\n3. Cook one side of the toast\n4. Turn toast over\n" 
    			+ "5. Put cheese on uncooked side of toast\n6. Cook until" +
    			" cheese" +	"is bubbling", "Snack", 10, "", "");*/
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
    
    public void setListView(ListView lb){
    	this.lv = lb;
    	lv.setTextFilterEnabled(true);
    }
    
    /** need it*/
	 String[] RECIPES = new String[]{"lol"};
   
}