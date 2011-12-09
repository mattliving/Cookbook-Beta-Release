package com.cookbook;

import android.app.TabActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.content.Intent;

import com.cookbook.RecipeListActivity;


public class CookbookActivity  extends TabActivity{

	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        final TabHost tabHost = getTabHost();

	        tabHost.addTab(tabHost.newTabSpec("Recipes")
	                .setIndicator("Recipes")
	                .setContent(new Intent(this, RecipeListActivity.class)));

	        tabHost.addTab(tabHost.newTabSpec("Bookmarks")
	                .setIndicator("Bookmarks")
	                .setContent(new Intent(this, BookmarkActivity.class)));
	        
	        // This tab sets the intent flag so that it is recreated each time
	        // the tab is clicked.
	        tabHost.addTab(tabHost.newTabSpec("tab3")
	                .setIndicator("MyRecipes")
	                .setContent(new Intent(this, MyRecipesActivity.class)
	                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
	    }
}
