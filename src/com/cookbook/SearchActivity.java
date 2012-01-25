

package com.cookbook;

import com.cookbook.RecipeList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class SearchActivity extends Activity
{  
        // UI elements
    Button mStartSearch;
    Spinner mMenuMode;
    EditText text;
    EditText mQueryAppData;
    CookbookDBAdapter mDbHelper;
    
    RecipeList list;
    ListView listvw;
    
    /** Array Adapter, needed to update the listview */
	protected ArrayAdapter<String> arrayadp;
    
     
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Inflate our UI from its XML layout description.
        setContentView(R.layout.search);
        
        mDbHelper = new CookbookDBAdapter(this);
        mDbHelper.open();
        
        // Get display items for later interaction
        mStartSearch = (Button) findViewById(R.id.buttonsearch);
        text = (EditText) findViewById(R.id.autoCompleteTextView1);
        listvw = (ListView) findViewById(R.id.listView1);
        
        list = new RecipeList();
        list.fetchAllRecipes(mDbHelper);
        
        /**
    	   * adding the list to the recipeArray used to display it
    	   */
    	  RECIPES = new String[list.size()];
    	  for (int i =0; i<list.size();i++){
    		  RECIPES[i] = list.getRecipe(i).getName()+"\nType: "+list.getRecipe(i).getType();
    		  System.out.println(list.getRecipe(i).getName());
    	  }
        
    	  
    	  // list_item is in /res/layout/ should be created
    	  arrayadp = new ArrayAdapter<String>(this, R.layout.list_item, RECIPES);
      	  listvw.setAdapter(arrayadp);



      	  
      	  listvw.setTextFilterEnabled(true);
        
    	  /**
    	   * Onclik show the info about the Recipe on a popup message
    	   */
    	  listvw.setOnItemClickListener(new OnItemClickListener() {
    	    public void onItemClick(AdapterView<?> parent, View view,
    	        int position, long id) {
    	      // When clicked, show a toast with the TextView text
    	    	Intent recIntent = new Intent(view.getContext(),ViewRecipeActivity.class);
    	    	// trying to send the recipe name to the new activity
    	    	recIntent.putExtra("recipeName",list.getRecipe(position).getName());
    	    	startActivity(recIntent);
    	      }
            }); 
    	  
    	  
    	  
    	  
    	  
        //TODO Populate items
       /* ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                            this, R.array.search_menuModes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMenuMode.setAdapter(adapter);
        */
        
        // Create listener for the menu mode dropdown.  We use this to demonstrate control
        // of the default keys handler in every Activity.  More typically, you will simply set
        // the default key mode in your activity's onCreate() handler.
        /*mMenuMode.setOnItemSelectedListener(
            new OnItemSelectedListener() {
                public void onItemSelected(
                        AdapterView<?> parent, View view, int position, long id) {
                    if (position == MENUMODE_TYPE_TO_SEARCH) {
                        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
                    } else {
                        setDefaultKeyMode(DEFAULT_KEYS_DISABLE);
                    }
                }

                public void onNothingSelected(AdapterView<?> parent) {
                    setDefaultKeyMode(DEFAULT_KEYS_DISABLE);
                }
            });
        */
        
        
        // Attach actions to buttons
        mStartSearch.setOnClickListener(
            new OnClickListener() {
                public void onClick(View v) {
                    onSearchRequested();
                }
            });
    }
    
    
    
    /** Handle the menu item selections */
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case 0:
            switch (mMenuMode.getSelectedItemPosition()) {
            case MENUMODE_SEARCH_KEY:
                new AlertDialog.Builder(this)
                    .setMessage("To invoke search, dismiss this dialog and press the search key" +
                                " (F5 on the simulator).")
                    .setPositiveButton("OK", null)
                    .show();
                break;
                
            case MENUMODE_MENU_ITEM:
                onSearchRequested();
                break;
                
            case MENUMODE_TYPE_TO_SEARCH:
                new AlertDialog.Builder(this)
                    .setMessage("To invoke search, dismiss this dialog and start typing.")
                    .setPositiveButton("OK", null)
                    .show();
                break;
                
            case MENUMODE_DISABLED:
                new AlertDialog.Builder(this)
                    .setMessage("You have disabled search.")
                    .setPositiveButton("OK", null)
                    .show();
                break;
            }
            break;
        case 1:
            clearSearchHistory();
            break;
        }
    
         return super.onOptionsItemSelected(item);
    }*/
    
    /**
     * This hook is called when the user signals the desire to start a search.
     * 
     * By overriding this hook we can insert local or context-specific data.
     * 
     * @return Returns true if search launched, false if activity blocks it
     */
    @Override
    public boolean onSearchRequested() {
        // If your application absolutely must disable search, do it here.
        if (text.length() <1) {
            list.clearList();
            list.fetchAllRecipes(mDbHelper);
        }
        else {
        list.clearList();
        String rName = text.getText().toString();
        list.fetchByName(mDbHelper, rName);
        list.fetchByName(mDbHelper, rName.toLowerCase());
        list.fetchByPatternName(mDbHelper, rName.toLowerCase()+"%");
        list.fetchByPatternName(mDbHelper, "%"+rName.toLowerCase()+"%");
        for (int i=1;i<rName.length()-1;i++)
        {
        	// TO-DO not working
        	String nName = new String(rName.substring(0, i-1));
        	nName.concat("_");
        	rName.concat(rName.substring(i+1, rName.length()-1));
        	list.fetchByPatternName(mDbHelper, nName.toLowerCase());
        }
        for (int i=rName.length()/2;i<rName.length()-1;i++)
        {
        	String nName = new String(rName.substring(i,rName.length()-1));
        	list.fetchByPatternName(mDbHelper, nName.toLowerCase()+"%");
        }
        }
        
        if (list.size()>0){
        	RECIPES = new String[list.size()];
      	  for (int i =0; i<list.size();i++){
      		  RECIPES[i] = list.getRecipe(i).getName()+"\nType: "+list.getRecipe(i).getType();
      		  System.out.println(list.getRecipe(i).getName());
      	  }
      	
        }
        else
        {
        	RECIPES = new String[1];
        	RECIPES[0] = "No results found";
        }
   
      	
        arrayadp = new ArrayAdapter<String>(this, R.layout.list_item, RECIPES);
        
    	  listvw.setAdapter(arrayadp);
    	  arrayadp.notifyDataSetChanged();
        
        // Returning true indicates that we did launch the search, instead of blocking it.
        return true;
    }
    
    
    
    
    
    /**
     * Any application that implements search suggestions based on previous actions (such as
     * recent queries, page/items viewed, etc.) should provide a way for the user to clear the
     * history.  This gives the user a measure of privacy, if they do not wish for their recent
     * searches to be replayed by other users of the device (via suggestions).
     * 
     * This example shows how to clear the search history for apps that use 
     * android.provider.SearchRecentSuggestions.  If you have developed a custom suggestions
     * provider, you'll need to provide a similar API for clearing history.
     * 
     * In this sample app we call this method from a "Clear History" menu item.  You could also 
     * implement the UI in your preferences, or any other logical place in your UI.
     */
    /*private void clearSearchHistory() {
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, 
                SearchSuggestionSampleProvider.AUTHORITY, SearchSuggestionSampleProvider.MODE);
        suggestions.clearHistory();
    }*/
    
    
    /** need it*/
	 String[] RECIPES = new String[]{"lol"};
    
}
