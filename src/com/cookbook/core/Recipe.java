/**
*@author Giulio Muntoni
*@version 1.0
*/

package com.cookbook.core;

/**
 * Class representing a Recipe Entity on the database
 */
public class Recipe {
	
	
	public enum TypeOfMeal { MAIN, SECOND, DESSERT, NULL }; // NOT FINAL
	public enum Season { SPRING, SUMMER, AUTUMN, WINTER, NULL};
	
	protected String mName;
	protected String mIngredients;
	protected String mPreparation;
	protected int identifier;
	protected String type;
	protected int cookingTime;
	protected String season;
	protected String mRegion;
	protected float mRating;
		
	
	/**
	 * Constructor
	 * @param mName
	 * @param mIngredients
	 * @param mPreparation
	 * @param identifier
	 * @param type
	 * @param cookingTime
	 * @param season
	 * @param mRegion
	 * @param mRating
	 */
	public Recipe(String mName,String mIngredients,String mPreparation,int identifier,
			 String type, int cookingTime, String season,String mRegion,float mRating){
	
	this.mName = new String(mName);
	this.mIngredients = new String(mIngredients); 
	this.mPreparation = new String(mPreparation);
	this.identifier = identifier;
	this.type = type;
	this.cookingTime = cookingTime;
	this.season = season;
	this.mRegion = new String(mRegion);
	this.mRating = mRating;
	
	}
	
	
	//Get Functions to access class protected fields 
	
	public String getName(){
		return this.mName;
	}
	
	public String getIngredients(){
		return this.mIngredients;
	}
	
	public String getPreparation(){
		return this.mPreparation;
	}
	
	public int getIdentifier(){
		return this.identifier;
	}
	
	public String getType(){
		return this.type;
	}
	
	public int getCookingTime(){
		return this.cookingTime;
	}
	
	public String getSeason(){
		return this.season;
	}
	
	public String getRegion(){
		return this.mRegion;
	}
	
	public float getRating(){
		return this.mRating;
	}
	
	
	

}
