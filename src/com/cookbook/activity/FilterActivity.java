package com.cookbook.activity;
import com.cookbook.*;
import com.cookbook.adapter.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ViewFlipper;

public class FilterActivity extends Activity {

    /** Database Adapter */
	private CookbookDBAdapter mDbHelper;
	/** List of Recipes */
	RecipeList list;
	
	/** Array Adapter, needed to update the listview */
	protected ArrayAdapter<String> arrayadp;
	
	
	 /*this is relating to the way the list items are displayed*/
	   public final static String ITEM_TITLE = "title";  
	   public final static String ITEM_CAPTION = "caption";  
	
		 String[] RECIPES = new String[]{" "};
	   
	String Spinval1;
	int cookingval;
	String Spinval2;
	String Spinval3;
	String Spinval4;
	String Spinval5;
	Cursor cursor;
	float ratingval;
	
    //This combines the title and caption to make an element which can be used with the separated list adapter
    public Map<String,?> createItem(String title, String caption) {  
        Map<String,String> item = new HashMap<String,String>();  
        item.put(ITEM_TITLE, title);  
        item.put(ITEM_CAPTION, caption);  
        return item;  
    }
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.filter);
    list = new RecipeList();
    mDbHelper = new CookbookDBAdapter(this);
    mDbHelper.open();


        //This is the drop-down menu in the sorting/organization screen (tab 3)
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                this, R.array.TypeOfRecipe, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        //This is the drop-down menu in the sorting/organization screen (tab 3)
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this, R.array.CookingTime, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        
        //This is the drop-down menu in the sorting/organization screen (tab 3)
        Spinner spinner3 = (Spinner) findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(
                this, R.array.TimeOfYear, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
        
        //This is the drop-down menu in the sorting/organization screen (tab 3)
        Spinner spinner4 = (Spinner) findViewById(R.id.spinner4);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(
                this, R.array.Country, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(adapter4);
        
        //This is the drop-down menu in the sorting/organization screen (tab 3)
        Spinner spinner5 = (Spinner) findViewById(R.id.spinner5);
        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(
                this, R.array.Ratings, android.R.layout.simple_spinner_item);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner5.setAdapter(adapter5);
        
        
        Button submit = (Button) findViewById(R.id.button1);
        final Button cancel = (Button) findViewById(R.id.button2);   
        
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Spinval1 = parent.getItemAtPosition(pos).toString();
                if(Spinval1.equals("All")) Spinval1 = null;
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Spinval2 = parent.getItemAtPosition(pos).toString();
                if(Spinval2.equals("All")) cookingval = 0;
                if(Spinval2.equals("10 mins")) cookingval = 10;
                else if(Spinval2.equals("20 mins")) cookingval = 20;
                else if(Spinval2.equals("30 mins")) cookingval = 30;
                else if(Spinval2.equals("1 hour")) cookingval = 60;
                else if(Spinval2.equals("2 hours")) cookingval = 120;
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        
        System.out.printf("val1 = %s\n", Spinval1);

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Spinval3 = parent.getItemAtPosition(pos).toString();
                if(Spinval3.equals("All")) Spinval3 = null;
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Spinval4 = parent.getItemAtPosition(pos).toString();
                if(Spinval4.equals("All")) Spinval4 = null;
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Spinval5 = parent.getItemAtPosition(pos).toString();
                if(Spinval5.equals("All"))ratingval = 0;
                else if(Spinval5.equals("1 Star")) ratingval = 1;
                else if(Spinval5.equals("2 Stars")) ratingval = 2;
                else if(Spinval5.equals("3 Stars")) ratingval = 3;
                else if(Spinval5.equals("4 Stars")) ratingval = 4;
                else if(Spinval5.equals("5 Stars")) ratingval = 5;

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

           finish();

            }
        });
        
        submit.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
            	Intent intent = new Intent(view.getContext(), RecipeListFilterActivity.class);
          		intent.putExtra("param1", Spinval1);
          		intent.putExtra("param2", cookingval);
          		intent.putExtra("param3", Spinval3);
          		intent.putExtra("param4", Spinval4);
          		intent.putExtra("param5", ratingval);
            	startActivityForResult(intent, 0);
            	
        	}
         });
            /*	Bundle params = new Bundle();
            	
            	  List<Map<String,?>> filter = new LinkedList<Map<String,?>>(); 	
            	
          	  cursor = mDbHelper.fetchRecipeFilter(Spinval1,cookingval,Spinval3,Spinval4,ratingval);
          	  
          	  RECIPES = new String[cursor.getCount()];

          	  if(cursor.getCount() > 0){
          	  for(int i = 0; i< cursor.getCount(); i++){
          		  RECIPES[i] = list.getRecipe(i).getName()+"\n"+list.getRecipe(i).getType();
          	  	  filter.add(createItem(cursor.getString(1), cursor.getString(2))); 
          	  	  cursor.moveToNext();
          	  }
          	  }
          	  else
          	  {
            	  RECIPES = new String[1];
            	  RECIPES[0] = "No Results";
          	  	  filter.add(createItem("No results", "")); 
          	  }
          	  
              // list_item is in /res/layout/ should be created
             // arrayadp = new ArrayAdapter<String>(this, R.layout.list_item, RECIPES);
          	 // setListAdapter(arrayadp);

          	 // final ListView lv = getListView();
          	 //lv.setTextFilterEnabled(true);

                


            }
        });
        
        */   
        

        }
}