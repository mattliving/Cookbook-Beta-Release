package com.cookbook;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.widget.Toast;
import android.database.Cursor;

import com.cookbook.*;
import com.cookbook.Recipe;

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
	
	/**
	 * Create Suggestions based on the recipes bookmarked
	 * @param bookmarks
	 * @return
	 */
	public RecipeList suggestFromBookmarks(RecipeList bookmarks){
		
		RecipeList list = new RecipeList();
		int nbookmarks = bookmarks.size();
		
		if (bookmarks.size()<1) return list;
		
		
		
		int type[] = {0,0,0,0,0,0}; //  0. null 1. Lunch 2. Dinner 3. Snack 4. Dessert 5. Breakfast
		int season[] = {0,0,0,0,0};  // 0-null , 1-spring , 2-summer , 3-autumn , 4-winter
		//int region[];
		int cookingtime =0;
		
		for (int i=0; i<nbookmarks;i++)
		{
			
			Recipe rec = bookmarks.getRecipe(i);
			
			if (rec.type.equalsIgnoreCase("null")) type[0]++;
			if (rec.type.equalsIgnoreCase("Lunch")) type[1]++;
			if (rec.type.equalsIgnoreCase("Dinner")) type[2]++;
			if (rec.type.equalsIgnoreCase("Snack")) type[3]++;
			if (rec.type.equalsIgnoreCase("Dessert")) type[4]++;
			if (rec.type.equalsIgnoreCase("Breakfast")) type[5]++;
			
			if (rec.season.equalsIgnoreCase("null")) season[0]++;
			if (rec.season.equalsIgnoreCase("spring")) season[1]++;
			if (rec.season.equalsIgnoreCase("summer")) season[2]++;
			if (rec.season.equalsIgnoreCase("autumn")) season[3]++;
			if (rec.season.equalsIgnoreCase("winter")) season[4]++;
			
			cookingtime+= rec.cookingTime;
			
		}
		
		
			Cursor cur;
			int typ = maxIntArray(type,6);
			int seas = maxIntArray(season,5);
			
			String typStr ="";
			String seasStr= "";
			cookingtime = cookingtime/nbookmarks;
			
			typStr = typeMatch(typ);
			seasStr = seasonMatch(seas);
			
			
			if ((cur = mDbHelper.fetchRecipe(typStr, seasStr, cookingtime/2*3))!= null)
			{
				
				cur.moveToFirst();
				while (!cur.isAfterLast()){
					list.addRecipe(cur);
					cur.moveToNext();
				}
			}
		
			// Now search for the second most bookmarked type and season with tim <cooktime*2
			typ = sndIntArray(type,6);
			seas = sndIntArray(season,5);
			
			typStr = typeMatch(typ);
			seasStr = seasonMatch(seas);
		
			if ((cur = mDbHelper.fetchRecipe(typStr, seasStr, cookingtime*2))!= null)
			{
				
				cur.moveToFirst();
				while (!cur.isAfterLast()){
					list.addRecipe(cur);
					cur.moveToNext();
				}
			}
			
			
		
		return list;
	}
	
	/**
	 *  Return the index of the array with the max value
	 * @param a Array Int
	 * @param n
	 * @return
	 */
	public int maxIntArray(int a[], int n)
	{
		int maxInd =0;
		for (int i=0; i<n;i++)
		{
			if (a[i]>= a[maxInd]) maxInd = i;
		}
		return maxInd;
	}
	
	public int sndIntArray(int a[], int n)
	{
		int maxInd = maxIntArray(a,n);
		int snd =0;
		for (int i=0; i<n;i++)
		{
			if (a[i]< a[maxInd]&& a[i]>= a[snd]) snd = i;
		}
		return snd;
	}
	
	public String typeMatch(int typ)
	{
		String typStr = "";
		if (typ ==0) typStr ="null"; 
		if (typ ==1) typStr ="Lunch";
		if (typ ==2) typStr ="Dinner";
		if (typ ==3) typStr ="Snack";
		if (typ ==4) typStr ="Dessert";
		if (typ ==5) typStr ="Breakfast";
		return typStr;
	}
	
	public String seasonMatch(int seas)
	{
		String seasStr = "";
		if (seas ==0) seasStr ="null";
		if (seas ==1) seasStr ="Spring";
		if (seas ==2) seasStr ="Summer";
		if (seas ==3) seasStr ="Autumn";
		if (seas ==4) seasStr ="Winter";
		return seasStr;
	}



}
