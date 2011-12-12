package com.cookbook;

import android.app.TabActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.content.Intent;

import com.cookbook.RecipeListActivity;

public class MyRecipesActivity  extends TabActivity{

	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        final TabHost tabHost = getTabHost();

	        tabHost.addTab(tabHost.newTabSpec("Recipes")
	                .setIndicator("Recipes")
	                .setContent(new Intent(this, RecipeListActivity.class)));

	        tabHost.addTab(tabHost.newTabSpec("Bookmarks")
	                .setIndicator("Bookmarks")
	                .setContent(new Intent(this, BookmarksActivity.class)));

	        // This tab sets the intent flag so that it is recreated each time
	        // the tab is clicked.
	        tabHost.addTab(tabHost.newTabSpec("UserRecipes")
	                .setIndicator("User Recipes")
	                .setContent(new Intent(this, UserRecipesActivity.class)));
	    }
}