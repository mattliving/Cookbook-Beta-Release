package com.cookbook;

import com.cookbook.adapter.CookbookDBAdapter;

public class Search {
	CookbookDBAdapter mDbHelper;
	
	public Search (CookbookDBAdapter mDbHelper){
		
		 this.mDbHelper = mDbHelper;
	    
	}
	
	public void searchByName(RecipeList list,String text){
		if (text.length() <1) {
            list.clearList();
            list.fetchAllRecipes(mDbHelper);
            return;
        }
        
        list.clearList();
        
        
        list.fetchByName(mDbHelper, text.toLowerCase());
	}
		
	public void searchByPatternName(RecipeList list,String text){
		if (text.length() <1) {
            list.clearList();
            list.fetchAllRecipes(mDbHelper);
            return;
        }

		
		/*list.fetchByPatternName(mDbHelper, text.toLowerCase()+"%"+
		"' AND recipeName<>"+"'"+text);*/
        list.fetchByPatternName(mDbHelper, "%"+text.toLowerCase()+"%"+
		"' AND recipeName<>"+"'"+text);
	}
		
	public void searchByName_TypingError(RecipeList list,String text){
		if (text.length() <1) {
            list.clearList();
            list.fetchAllRecipes(mDbHelper);
            return;
        } 
        
        for (int i=1;i<text.length()-1;i++)
        {
        	// TO-DO not working
        	String nName = new String(text.substring(0, i-1));
        	nName.concat("_");
        	text.concat(text.substring(i+1, text.length()-1));
        	list.fetchByPatternName(mDbHelper, nName.toLowerCase()+
        			"' AND recipeName<>"+"'"+text);
        }
		
	}
	
	public void searchByName_SubString(RecipeList list,String rName){
		if (rName.length() <1) {
            list.clearList();
            list.fetchAllRecipes(mDbHelper);
            return;
        }
		for (int i=rName.length()/2;i<rName.length()-1;i++)
		{
    	String nName = new String(rName.substring(i,rName.length()-1));
    	list.fetchByPatternName(mDbHelper, nName.toLowerCase()+
    			"' AND recipeName<>"+"'"+rName);
		}
	}
}
