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
        /*
        mDbHelper.createRecipe("pizza","bake in the oven", "Snack", 10, "null", "Italy",0);
        mDbHelper.createRecipe("hamburger","bake in the oven", "Snack", 10, "null", "Usa",0);
        mDbHelper.createRecipe("pasta","boil it", "Lunch", 10, "null", "Italy",0);
        mDbHelper.createRecipe("fish and Chips","fry in the oil", "Snack", 10, "spring", "England",0);
        mDbHelper.createRecipe("hash browns","bake in the oven", "Dinner", 10, "summer", "England",0);
        mDbHelper.createRecipe("fish","bake in the oven", "Dinner", 10, "null", "World",0);
        mDbHelper.createRecipe("noodles","boil", "null", 10, "winter", "China",0);
        mDbHelper.createRecipe("sushi","no", "Dinner", 10, "null", "Japan",0);
        mDbHelper.createRecipe("kebab","bake in the oven", "Snack", 10, "winter", "Turkey",0);
        mDbHelper.createRecipe("pie","bake in the oven", "Dessert", 10, "spring", "World",0);
        mDbHelper.createRecipe("meatballs","bake in the oven", "Lunch", 10, "winter", "Usa",0);
        mDbHelper.createRecipe("cake","bake in the oven", "Dessert", 10, "spring", "World",0);
        mDbHelper.createRecipe("soup","bake in the oven", "Snack", 10, "null", "Italy",0);
        mDbHelper.createRecipe("veggie soup","bake in the oven", "Snack", 10, "null", "Usa",0);
        mDbHelper.createRecipe("roasted beef","boil it", "Breakfast", 10, "null", "Italy",0);
        mDbHelper.createRecipe("tuna","fry in the oil", "Snack", 10, "spring", "England",0);
        mDbHelper.createRecipe("carrots","bake in the oven", "Dinner", 10, "summer", "England",0);
        mDbHelper.createRecipe("boar","bake in the oven", "Dinner", 10, "null", "World",0);
        mDbHelper.createRecipe("ice cream sandwich","boil", "null", 10, "winter", "China",0);
        mDbHelper.createRecipe("frozen yogurt","no", "Breakfast", 10, "null", "Japan",0);
        mDbHelper.createRecipe("gingerbread","bake in the oven", "Snack", 10, "winter", "Turkey",0);
        mDbHelper.createRecipe("focaccia","bake in the oven", "Dessert", 10, "spring", "World",0);
        mDbHelper.createRecipe("paninis","bake in the oven", "Lunch", 10, "winter", "Usa",0);
        mDbHelper.createRecipe("pudding","bake in the oven", "Dessert", 10, "spring", "World",0);
        mDbHelper.close();
        */
        /*
         * setOnItemClickListener
         */
        
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	Intent intent;
            	switch (position) {
	            	case 0:
	            		intent = new Intent(v.getContext(), SuggestionActivity.class);
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