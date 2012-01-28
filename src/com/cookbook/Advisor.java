package com.cookbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.widget.Toast;

import com.cookbook.*;

/**
 * This class implements methods for recipes suggestions
 * @author Giulio
 *
 */
public class Advisor {

	CookbookDBAdapter mDbHelper;
	
	public Advisor(CookbookDBAdapter mDb){
		this.mDbHelper = mDb;
	}
	
	public RecipeList suggestFromBookmarks(RecipeList bookmarks){
		
		RecipeList list = new RecipeList();
		int nbookmarks = bookmarks.size();
		
		if (bookmarks.size()<1) return list;
		
		
		
		int type[];
		int season[] = new int[4];  // 0-winter , 1-spring , 2-summer , 3-autumn
		//int region[];
		int cookingtime;
		
		for (int i=0; i<nbookmarks;i++){
			
			//Training!
		}
		
		
		return list;
	}



}
