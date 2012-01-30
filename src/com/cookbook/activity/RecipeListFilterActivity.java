package com.cookbook.activity;
import com.cookbook.*;
import com.cookbook.adapter.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.cookbook.RecipeList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

//Trying commit and removing cookbook folder

public class RecipeListFilterActivity extends ListActivity {
    
	
	 /*this is relating to the way the list items are displayed*/
   public final static String ITEM_TITLE = "title";  
   public final static String ITEM_CAPTION = "caption";  
	
	/**
	 * Database Adapter
	 */
	protected CookbookDBAdapter mDbHelper;
	
	/** List of Recipes */
	protected RecipeList list = new RecipeList();
	
	/** ListView */
	protected  ListView lv;

    //This combines the title and caption to make an element which can be used with the separated list adapter
    public Map<String,?> createItem(String title, String caption) {  
        Map<String,String> item = new HashMap<String,String>();  
        item.put(ITEM_TITLE, title);  
        item.put(ITEM_CAPTION, caption);  
        return item;  
    }
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        String param1;
        int param2;
        String param3;
        String param4;
        float param5;
        
        Intent sender = getIntent();
        param1= sender.getExtras().getString("param1");
        param2= sender.getExtras().getInt("param2");
        param3= sender.getExtras().getString("param3");
        param4= sender.getExtras().getString("param4");
        param5= sender.getExtras().getFloat("param5");
        
        System.out.printf("%s\n", param1);
        //System.out.printf("%d\n", param2);
        //System.out.printf("%s\n", param3);
        //System.out.printf("%s\n", param4);
        //System.out.printf("%d\n", param5);
        
        //Initialise the DB
        mDbHelper = new CookbookDBAdapter(this);
        mDbHelper.open();
        //createRecipe();
        //createIngredient();
        //createRecipeIngredients();
        
        
        /*
         * Add the database entries to the list
         */
        list.fetchfilterRecipes(mDbHelper ,param1,param2,param3,param4,param5);
        
        /**
         * Debugging messages in android!
         */
        Log.d("MyDebug", String.valueOf(list.size()));
        
    	  List<Map<String,?>> recipes = new LinkedList<Map<String,?>>(); 	
        
        /**
  	   * adding the list to the recipeArray used to display it
  	   */
  	  RECIPES = new String[list.size()];
  	  for (int i =0; i<list.size();i++){
  		  RECIPES[i] = list.getRecipe(i).getName()+"\n"+list.getRecipe(i).getType();
  	  	  recipes.add(createItem(list.getRecipe(i).getName(), list.getRecipe(i).getType())); 

  	  }
	  
	  
	  //Create our list and custom adapter  
	  SeparatedListAdapter adapter = new SeparatedListAdapter(this); 
	  adapter.addSection("Filter Results", new SimpleAdapter(this, recipes, R.layout.list_complex, 
			  new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));


  	  lv = getListView();
      lv.setAdapter(adapter);
  	  lv.setTextFilterEnabled(true);

        
        

  	/**
	   * Onclik show the info about the Recipe on a popup message
	   */
	  lv.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view,
	        int position, long id) {
	      // When clicked, show a toast with the TextView text
	     /* Toast.makeText(getApplicationContext(), 
	    "Ingredients: "+list.getRecipe(position).getIngredients()+"\nPreparation: "+list.getRecipe(position).getPreparation()
	    +"\nType: "+list.getRecipe(position).getType()+"\nRegion: "+list.getRecipe(position).getRegion(),
	          Toast.LENGTH_SHORT).show();
	          */
	    	//Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_SHORT).show();
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