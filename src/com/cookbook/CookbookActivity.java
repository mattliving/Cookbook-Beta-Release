package com.cookbook;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cookbook.facebook.BaseRequestListener;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionEvents;
import com.facebook.android.SessionEvents.AuthListener;
import com.facebook.android.SessionEvents.LogoutListener;
import com.facebook.android.SessionStore;

public class CookbookActivity extends Activity {
	/** Called when the activity is first created. */
	//private Facebook mFacebook;
    public static final String APP_ID = "263632013686454";
    private Handler mHandler;
    ProgressDialog dialog;
    private TextView mSearchBar;
    private TextView mText;
    private ImageView mUserPic;
	public CookbookDBAdapter mDbHelper;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // Create the Facebook Object using the app id.
        Utility.mFacebook = new Facebook(APP_ID);
        // Instantiate the asynrunner object for asynchronous api calls.
        Utility.mAsyncRunner = new AsyncFacebookRunner(Utility.mFacebook);

        // Restore session if one exists
        SessionStore.restore(Utility.mFacebook, this);
        SessionEvents.addAuthListener(new FbAPIsAuthListener());
        SessionEvents.addLogoutListener(new FbAPIsLogoutListener());  
        
        //if (Utility.mFacebook.isSessionValid()) {
        	Utility.mFacebook.authorize(this, new DialogListener() {
                public void onComplete(Bundle values) {}

                public void onFacebookError(FacebookError error) {}
                public void onError(DialogError e) {}
                public void onCancel() {}
            });
        //}
        
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
            			String query = "SELECT name, current_location, uid, pic_square FROM user WHERE uid IN (SELECT uid2 FROM friend WHERE uid1=me()) order by name";
	            		Bundle params = new Bundle();
                        params.putString("method", "fql.query");
                        params.putString("query", query);
	            		Utility.mAsyncRunner.request(null, params, new FriendsRequestListener());
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

        Utility.mFacebook.authorizeCallback(requestCode, resultCode, data);
    }

    /*
     * Request user name, and picture to show on the main screen.
     */
    public void requestUserData() {
        Bundle params = new Bundle();
        params.putString("fields", "name, picture");
        Utility.mAsyncRunner.request("me", params, new UserRequestListener());
    }

    /*
     * Callback for fetching current user's name, picture, uid.
     */
    public class UserRequestListener extends BaseRequestListener {
        public void onComplete(final String response, final Object state) {
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(response);

                final String picURL = jsonObject.getString("picture");
                final String name = jsonObject.getString("name");
                Utility.userUID = jsonObject.getString("id");

                mHandler.post(new Runnable() {
                    public void run() {
                        mText.setText(name);
                        mUserPic.setImageBitmap(Utility.getBitmap(picURL));
                    }
                });

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
    
    /*
     * Callback after friends are fetched via me/friends or fql query.
     */
    public class FriendsRequestListener extends BaseRequestListener {
        public void onComplete(final String response, final Object state) {
            //dialog.dismiss();
            Intent myIntent = new Intent(getApplicationContext(), FriendsList.class);
            myIntent.putExtra("API_RESPONSE", response);
            myIntent.putExtra("METHOD", "fql");
            startActivity(myIntent);
        }

        public void onFacebookError(FacebookError error) {
            //dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Facebook Error: " + error.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }
    
    /*
     * The Callback for notifying the application when authorization succeeds or
     * fails.
     */

    public class FbAPIsAuthListener implements AuthListener {
        public void onAuthSucceed() {
            requestUserData();
        }

        public void onAuthFail(String error) {
            mText.setText("Login Failed: " + error);
        }
    }
    
    /*
     * The Callback for notifying the application when log out starts and
     * finishes.
     */
    public class FbAPIsLogoutListener implements LogoutListener {
        public void onLogoutBegin() {
            mText.setText("Logging out...");
        }

        public void onLogoutFinish() {
            mText.setText("You have logged out! ");
            mUserPic.setImageBitmap(null);
        }
    }

}