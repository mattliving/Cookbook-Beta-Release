package com.cookbook;

import android.app.Application;

import com.cookbook.CookbookDBAdapter;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;

/* Global variables to be used across activities */
public class GlobalVars extends Application {
	//public static CookbookDBAdapter DbHelper = new CookbookDBAdapter(this);
	public static final String APP_ID = "263632013686454";
	public static Facebook facebook = new Facebook(APP_ID);
	public static AsyncFacebookRunner aSyncRunner = new AsyncFacebookRunner(facebook);
}
