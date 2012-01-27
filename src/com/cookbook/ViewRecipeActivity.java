package com.cookbook;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cookbook.facebook.BaseDialogListener;
import com.cookbook.facebook.UpdateStatusResultDialog;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;

public class ViewRecipeActivity extends Activity
{
	private CookbookDBAdapter mDbHelper;
	
	private Facebook mFacebook;
	private Cursor recipe;
	private Cursor ingredients;
	
	private long recipeID;
	private String recipeName;
	private String method;
	private String mealType;
	private String duration;
	private String timeOfYear;
	private String region;
	
	Resources myResources;
	readFile rd;
	/** List of Bookmarks */
	protected RecipeList bookmarks = new RecipeList();
	
	public static final String HACK_ICON_URL = "http://www.facebookmobileweb.com/hackbook/img/facebook_icon_large.png";

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);	
        setContentView(R.layout.view_recipe);
        mFacebook = GlobalVars.facebook;
        mDbHelper = new CookbookDBAdapter(this);
        mDbHelper.open();
        Bundle b = getIntent().getExtras();
        recipeName = b.getString("recipeName");
        
        myResources = getResources();
        /*
         * Read the bookmarks list.
         */
        FileInputStream fos;
		try {
			fos = openFileInput("bookmarks");
			rd = new readFile();
	        bookmarks.fetchFromIDs(rd.readIDs(fos),mDbHelper);
	        try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			Toast.makeText(getApplicationContext(),"not found",Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
        
        
        //If it doesn't find the recipe crashes
        recipe = mDbHelper.fetchRecipe(recipeName);
        ingredients = mDbHelper.fetchRecipeIngredient(1);
        
        int count = ingredients.getCount();
        TextView new6 = (TextView)this.findViewById(R.id.new6);
        new6.setText(String.valueOf(count));
        
        Button shareFB = (Button) findViewById(R.id.ShareToFacebook);
        //Button shareTwitter = (Button) findViewById(R.id.ShareToTwitter);
        CheckBox bookmark = (CheckBox) findViewById(R.id.bookmark);
        RatingBar rating = (RatingBar) findViewById(R.id.ratingBar1);
        
        //String[][] ingredientslist = new String[count][3];
        recipeID = recipe.getLong(0);
        recipeName = recipe.getString(1);
        method = recipe.getString(2);
        mealType = recipe.getString(3);
        duration = recipe.getString(4);
        timeOfYear = recipe.getString(5);
        region = recipe.getString(6);

        setLabels(recipeName, method, mealType, duration, timeOfYear, region);
        
        if (bookmarks.contains(recipeName)) {
        	bookmark.setChecked(true);
        	
        }
      
        /**
         * Facebook Share Button
         */
        shareFB.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	//Post on current user's wall
            	Bundle params = new Bundle();
                /*params.putString("caption", getString(R.string.app_name));
                params.putString("description", getString(R.string.app_desc));
                params.putString("picture", HACK_ICON_URL);
                params.putString("name", getString(R.string.app_action));
                params.putString("link", getString(R.string.cookbookFBpage));*/
            	params.putString("name", recipeName);
            	params.putString("caption", recipeName);
            	params.putString("description", method);
            	params.putString("picture", HACK_ICON_URL);
                mFacebook.dialog(v.getContext(), "feed", params, new PostDialogListener());
            }
        });
        
        /*shareTwitter.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	
            }
        });*/
        
        /** 
         * BOOKMARK BUTTON 
         * */
        bookmark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				
				/**
				 * if isChecked
				 * 		add it to the bookmark list and save the list.
				 * else
				 * 		remove from the list(loaded from file) and save it
				 */
				
				
				if (isChecked)
				{
					bookmarks.addRecipe(new Recipe(recipeName,"",method,recipeID,mealType,
							0,timeOfYear,region,0f));
					
					
				}
				else
				{
					bookmarks.removeRecipe(recipeName);
					
				}
				FileOutputStream ros;
				try {
					ros = openFileOutput("bookmarks", Context.MODE_PRIVATE);
					rd.writeIDs(bookmarks,ros);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
        });
        
        /**
         * RATING BUTTON
         */
        //TO_DO
        
    }
    
    
    
    
    /*protected void onStart() {};
    
    protected void onRestart() {};

    protected void onResume() {
    	mDbHelper.open();
    };

    protected void onPause() {
    	mDbHelper.close();
    };

    protected void onStop() {};

    protected void onDestroy() {};*/
    
    // Print recipe fields to TextViews
    private void setLabels(String recipeName, String method, String mealType, String duration2, String timeOfYear, String region)
    {
        TextView txtRecipeName = (TextView)this.findViewById(R.id.viewRecipeName);
        txtRecipeName.setText(recipeName);
        TextView txtMethod = (TextView)this.findViewById(R.id.viewMethod);
        txtMethod.setText(method);
        TextView txtMealType = (TextView)this.findViewById(R.id.viewMealType);
        txtMealType.setText(mealType);
        TextView txtDuration = (TextView)this.findViewById(R.id.viewDuration);
        txtDuration.setText(duration2);
        TextView txtTimeOfYear = (TextView)this.findViewById(R.id.viewTimeOfYear);
        txtTimeOfYear.setText(timeOfYear);
        TextView txtRegion = (TextView)this.findViewById(R.id.viewRegion);
        txtRegion.setText(region);
    }
    
    public class PostDialogListener extends BaseDialogListener {
        public void onComplete(Bundle values) {
            final String postId = values.getString("post_id");
            if (postId != null) {
                new UpdateStatusResultDialog(ViewRecipeActivity.this, "Update Status executed", values)
                        .show();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "No wall post made",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        public void onFacebookError(FacebookError error) {
            Toast.makeText(getApplicationContext(), "Facebook Error: " + error.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }

        public void onCancel() {
            Toast toast = Toast.makeText(getApplicationContext(), "Update status cancelled",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    
    
   
    
}