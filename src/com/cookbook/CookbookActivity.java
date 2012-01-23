package com.cookbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class CookbookActivity extends Activity {
	/** Called when the activity is first created. */
	private Facebook mFacebook;
	public CookbookDBAdapter mDbHelper;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mFacebook = GlobalVars.facebook;
        mFacebook.authorize(this, new DialogListener() {
            public void onComplete(Bundle values) {}

            public void onFacebookError(FacebookError error) {}
            public void onError(DialogError e) {}
            public void onCancel() {}
        });
        
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
        
        mDbHelper = new CookbookDBAdapter(this);
        mDbHelper.open();
        /*mDbHelper.createRecipe("pizza","bake in the oven", "First", 10, "Null", "Italy");
        mDbHelper.createRecipe("hamburger","bake in the oven", "First", 10, "Null", "Usa");
        mDbHelper.createRecipe("pasta","boil it", "First", 10, "Null", "Italy");
        mDbHelper.createRecipe("fish and Chips","fry in the oil", "First", 10, "Null", "England");
        mDbHelper.createRecipe("hash browns","bake in the oven", "First", 10, "Null", "England");
        mDbHelper.createRecipe("fish","bake in the oven", "First", 10, "Null", "World");
        mDbHelper.createRecipe("noodles","boil", "First", 10, "Null", "China");
        mDbHelper.createRecipe("sushi","no", "First", 10, "Null", "Japan");
        mDbHelper.createRecipe("kebab","bake in the oven", "First", 10, "Null", "Turkey");
        mDbHelper.createRecipe("pie","bake in the oven", "First", 10, "Null", "World");
        mDbHelper.createRecipe("meatballs","bake in the oven", "First", 10, "Null", "Usa");
        mDbHelper.createRecipe("cake","bake in the oven", "First", 10, "Null", "World");
        */
        mDbHelper.close();
        
        /*
         * setOnItemClickListener
         */
        
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	Intent intent;
            	switch (position) {
	            	case 0:
	            		intent = new Intent(v.getContext(), AddRecipeActivity.class);
	                	startActivityForResult(intent, 0);
	                	break;
	            	case 1:
	            		intent = new Intent(v.getContext(), MyRecipesActivity.class);
	                	startActivityForResult(intent, 1);
	                	break;
	            	case 2:
	            		intent = new Intent(v.getContext(), SearchActivity.class);
	                	startActivityForResult(intent, 2);
	                	break;
	            	case 3:
	            		intent = new Intent(v.getContext(), SettingsActivity.class);
	                	startActivityForResult(intent, 3);
	                	break;
	            	case 4:
	            		intent = new Intent(v.getContext(), SearchActivity.class);
	                	startActivityForResult(intent, 4);
	                	break;
	            	default:
	            		break;
            	}
            }
        });
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mFacebook.authorizeCallback(requestCode, resultCode, data);
    }
}