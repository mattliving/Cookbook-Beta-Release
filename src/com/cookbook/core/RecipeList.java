/**
*@author Giulio Muntoni
*@version 1.0
*/

package com.cookbook.core;

import java.util.Vector;

import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.cookbook.CookBookDbAdapter;
import com.cookbook.core.Recipe.Season;
import com.cookbook.core.Recipe.TypeOfMeal;


/** 
 * Class representing a list of Recipes classes
 * @author Giulio
 *
 */
public class RecipeList {

	/** 
	 * The Vector storing the references to Recipes instances 
	 * */
	protected Vector<Recipe> list;
	
	/**
	 * Constructor
	 */
	public RecipeList(){
		list = new Vector<Recipe>();
	}
	
	public void addRecipe(Recipe recipe){
		list.add(recipe);
	}
	
	/**
	 * Removes the instance of Recipe from the list
	 * @deprecated We should overload this function with other kind of input as recipe name,index etc
	 * @param recipe
	 */
	public void removeRecipe(Recipe recipe){
		list.remove(recipe);
	}
	/**
	 * Removes all elements putting the size of the vector at 0
	 */
	public void clearList(){
		list.removeAllElements();
	}
	
	/**
	 * TO-DO
	 */
	public void mergeList(){
	
	}
	
	public Recipe getRecipe(int index){
		return list.get(index);
	}
	
	/**
	 * Find if the list contains the same instance of recipes, it doesn't check
	 * if the two classes have the same values
	 * @param recipe
	 * @return
	 */
	public boolean contains(Recipe recipe){
		return list.contains(recipe);
	}
	
	/**
	 * TO-DO
	 */
	public void orderAlphabetical(){
	
	}
	
	public int size(){
		return list.size();
	}
	
	
	/**
	 * Parse one cursor row into a Recipe and add it to the list
	 * @param cursor
	 */
	public void addRecipe(Cursor cursor){
		
		int identifier = cursor.getInt(0);
		String mName = cursor.getString(1);
		String mPreparation = cursor.getString(2);
		String type= cursor.getString(3);
		int cookingTime = cursor.getInt(4);
		String season = cursor.getString(5);
		String mRegion = cursor.getString(6);
		/*
		 * Commented untile these fields will be part of the database
		 */
		//String mIngredients = cursor.getString(7);
		//float mRating = cursor.getFloat(8);
		/*
		 *  NOT FINAL 
		 */
		addRecipe(new Recipe(mName," ",mPreparation,identifier,
				type,cookingTime,season,mRegion,1f));
		
	}
	
	
	
	/**
	 * Build a list with the whole database. to be used only in ALPHA-BETA
	 * @param adpt the cookbook adapter
	 */
	public void fetchAllRecipes(CookBookDbAdapter adpt){
		
		Cursor cursor = adpt.fetchAllRecipes();
		
		/**
		 * WHY 84 ROWS if the recipes insterted are 3?
		 */
		Log.d("MyDebug", String.valueOf(cursor.getCount()));
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			addRecipe(cursor);
			
		cursor.moveToNext();
		}
	
		
	}
	
	/**
	 * Add Recipes to the list from a list of database IDs
	 * @param ids
	 * @param adpt
	 */
	
	public void fetchFromIDs(Vector<Long> ids, CookBookDbAdapter adpt){
		
	if (ids.size() ==0) return;	
		for (int i=0;i<ids.size();i++){
			
			Cursor cur = null;
			try {
				cur = adpt.fetchRecipe(ids.get(i));
			} catch (SQLException e) {
				
			}
			if (cur !=null) addRecipe(cur);
			
		}
		
	}
	
}
