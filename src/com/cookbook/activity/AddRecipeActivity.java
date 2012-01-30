package com.cookbook.activity;

import com.cookbook.*;
import com.cookbook.adapter.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TableLayout;
import android.widget.TableRow;

public class AddRecipeActivity extends Activity {
	private CookbookDBAdapter mDbHelper;
	int counterIngredient = (int) getItemId(R.id.IngredientBox0);
	int counterAmount = (int) getItemId(R.id.AmountBox0);
	int counterMeasurement = (int) getItemId(R.id.WeightMeasurement0);
	int counterEdit = (int) getItemId(R.id.EditButton0);
	int counterRemove = (int) getItemId(R.id.RemoveButton0);
	int counterRow = (int) getItemId(R.id.Ingredients1);
	int remember = (int) 0x7f07007f - (int) getItemId(R.id.RemoveButton0);
	
	public long getItemId(int position) {  
	     return position;  
	} 
    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipes);
        mDbHelper = new CookbookDBAdapter(this);
        mDbHelper.open();
        
        TableRow rowAmount = (TableRow)findViewById(R.id.Amount1);
        TableRow rowMeasurement = (TableRow)findViewById(R.id.Measurement1);
        rowAmount.setId(counterRow+remember);
        rowMeasurement.setId(counterRow+remember+1);
        
        ImageButton homeButton = (ImageButton) findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(v.getContext(), CookbookActivity.class);
            	startActivity(intent);
            }
        });
        
        ImageButton searchButton = (ImageButton) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(v.getContext(), SearchActivity.class);
            	startActivity(intent);
            }
        });
        
        TabHost tabHost=(TabHost)findViewById(R.id.tabHost);
        tabHost.setup();

        TabSpec spec1=tabHost.newTabSpec("Summary");
        spec1.setIndicator("Summary",getResources().getDrawable(R.drawable.ic_launcher));
        spec1.setContent(R.id.tab1);

        TabSpec spec2=tabHost.newTabSpec("Ingredients");
        spec2.setIndicator("Ingredients",getResources().getDrawable(R.drawable.ic_launcher));
        spec2.setContent(R.id.tab2);

        TabSpec spec3=tabHost.newTabSpec("Method");
        spec3.setIndicator("Method",getResources().getDrawable(R.drawable.ic_launcher));
        spec3.setContent(R.id.tab3);

        tabHost.addTab(spec1);
        tabHost.addTab(spec2);
        tabHost.addTab(spec3);
        
        Button mainAdd = (Button) findViewById(R.id.AddIngredients);
        
        mainAdd.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                EditText Ingredient = (EditText)findViewById(counterIngredient);
                EditText Amount = (EditText)findViewById(counterAmount);
                String IngredientString = Ingredient.getText().toString();
                String AmountString = Amount.getText().toString();
                if ((IngredientString != null) && (IngredientString.length() > 0)) {
                	if ((AmountString != null) && (AmountString.length() > 0)) {
                    	//What to do when ingredient is added completely...
                		IngredientString = null;
                		AmountString = null;
                		EditText IngredientED = (EditText) findViewById(counterIngredient);
                		EditText AmountED = (EditText) findViewById(counterAmount);
                		Spinner MeasurementS = (Spinner) findViewById(counterMeasurement);
                		Button ButtonEdit = (Button) findViewById(counterEdit);
                		Button ButtonRemove = (Button) findViewById(counterRemove);
                		IngredientED.setFocusable(false);
                		AmountED.setFocusable(false);
                		MeasurementS.setFocusable(false);
                		MeasurementS.setEnabled(false);
                		IngredientED.setTextColor(Color.LTGRAY);
                		AmountED.setTextColor(Color.LTGRAY);
                		ButtonEdit.setVisibility(View.VISIBLE);
                		ButtonEdit.setOnClickListener(listenerOfEditButton);
                		ButtonRemove.setVisibility(View.VISIBLE);
                		ButtonRemove.setOnClickListener(listenerOfRemoveButton);
                		counterIngredient += 25;
                		counterAmount += 25;
                		counterMeasurement += 25;
                		counterEdit += 25;
                		counterRow += 25;
                		counterRemove += 25;
                		TableLayout table = (TableLayout) findViewById(R.id.IngredientsTable);
                		
                		TableRow rowIngredient = new TableRow(AddRecipeActivity.this);
                		EditText tIngredient = new EditText(AddRecipeActivity.this);
                		tIngredient.setHint("Ingredient");
                		tIngredient.setInputType(InputType.TYPE_CLASS_TEXT);
                		tIngredient.setId(counterIngredient);
                		Button editButton = new Button(AddRecipeActivity.this);
                		editButton.setVisibility(View.INVISIBLE);
                		editButton.setText("Edit");
                		editButton.setId(counterEdit);
                		editButton.setOnClickListener(listenerOfEditButton);
                		rowIngredient.setId(counterRow);
                		rowIngredient.addView(tIngredient);
                		rowIngredient.addView(editButton);
                		table.addView(rowIngredient,new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, (float) 1));
                		
                		//counterRow += 25;
                		TableRow rowAmount = new TableRow(AddRecipeActivity.this);
                		EditText tAmount = new EditText(AddRecipeActivity.this);
                		tAmount.setHint("Amount (weight, volume etc.)");
                		tAmount.setInputType(InputType.TYPE_CLASS_NUMBER);
                		tAmount.setId(counterAmount);
                		Button removeButton = new Button(AddRecipeActivity.this);
                		removeButton.setVisibility(View.INVISIBLE);
                		removeButton.setText("Remove");
                		removeButton.setId(counterRemove);
                		removeButton.setOnClickListener(listenerOfRemoveButton);
                		rowAmount.setId(counterRow+remember);
                		rowAmount.addView(tAmount);
                		rowAmount.addView(removeButton);
                		table.addView(rowAmount,new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, (float) 1));
                		
                		//counterRow += 25;
                		TableRow rowMeasurement = new TableRow(AddRecipeActivity.this);
                		Spinner tMeasurement = new Spinner(AddRecipeActivity.this);
                		tMeasurement.setPrompt("Measurement");
                		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                				AddRecipeActivity.this, R.array.WeightMeasurement, android.R.layout.simple_spinner_item);
                		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                	    tMeasurement.setAdapter(adapter);
                		tMeasurement.setId(counterMeasurement);
                		rowMeasurement.setId(counterRow+remember+1);
                		rowMeasurement.addView(tMeasurement);
                		table.addView(rowMeasurement,new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, (float) 1));
                    } else {
                    	IngredientString = null;
                		AmountString = null;
                    	new AlertDialog.Builder(AddRecipeActivity.this)
                        .setTitle("Fill in all fields")
                        .setMessage("Please enter an amount")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) { 
                            	dialog.dismiss();
                            }
                         })
                         .show();
                    }
                } else {
                	IngredientString = null;
            		AmountString = null;
                	new AlertDialog.Builder(AddRecipeActivity.this)
                    .setTitle("Fill in all fields")
                    .setMessage("Please enter an ingredient")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) { 
                        	dialog.dismiss();
                        }
                     })
                     .show();
                }
            }
        });
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
        
    private OnClickListener listenerOfEditButton = new OnClickListener()
    {
        public void onClick(View v) {
        	int ID = ((Button)v).getId();
        	int ItemID = 0;
        	int difference = 0;
        	difference = counterIngredient - counterEdit;
        	ItemID = ID + difference;
        	EditText editIngredient = (EditText) findViewById(ItemID);
        	editIngredient.setFocusable(true);
        	editIngredient.setTextColor(Color.BLACK);
        	difference = counterAmount - counterEdit;
        	ItemID = ID + difference;
        	EditText editAmount = (EditText) findViewById(ItemID);
        	editAmount.setFocusable(true);
        	editAmount.setTextColor(Color.BLACK);
        	difference = counterMeasurement - counterEdit;
        	ItemID = ID + difference;
        	Spinner editMeasurement = (Spinner) findViewById(ItemID);
        	editMeasurement.setFocusable(true);
        	editMeasurement.setEnabled(true);
        	Button editToDone = (Button) findViewById(ID);
        	editToDone.setText("Done");
        	editToDone.setOnClickListener(listenerOfDoneButton);
        }
    };
    
    private OnClickListener listenerOfDoneButton = new OnClickListener()
    {
        public void onClick(View v) {
            int ID = ((Button)v).getId();
        	int IngredientsID = 0;
        	int AmountID = 0;
        	int MeasurementID = 0;
        	int difference = 0;
        	difference = counterIngredient - counterEdit;
        	IngredientsID = ID + difference;
        	difference = counterAmount - counterEdit;
        	AmountID = ID + difference;
        	difference = counterMeasurement - counterEdit;
        	MeasurementID = ID + difference;
        	EditText Ingredient = (EditText)findViewById(IngredientsID);
            EditText Amount = (EditText)findViewById(AmountID);
            String IngredientString = Ingredient.getText().toString();
            String AmountString = Amount.getText().toString();
            if ((IngredientString != null) && (IngredientString.length() > 0)) {
            	if ((AmountString != null) && (AmountString.length() > 0)) {
                	//What to do when ingredient is added completely...
            		IngredientString = null;
            		AmountString = null;
            		EditText IngredientED = (EditText) findViewById(IngredientsID);
            		EditText AmountED = (EditText) findViewById(AmountID);
            		Spinner MeasurementS = (Spinner) findViewById(MeasurementID);
            		Button ButtonEdit = (Button) findViewById(ID);
            		IngredientED.setFocusable(false);
            		AmountED.setFocusable(false);
            		MeasurementS.setFocusable(false);
            		MeasurementS.setEnabled(false);
            		IngredientED.setTextColor(Color.LTGRAY);
            		AmountED.setTextColor(Color.LTGRAY);
            		ButtonEdit.setVisibility(View.VISIBLE);
            		ButtonEdit.setOnClickListener(listenerOfEditButton);
            		ButtonEdit.setText("Edit");
                } else {
                	new AlertDialog.Builder(AddRecipeActivity.this)
                    .setTitle("Fill in all fields")
                    .setMessage("Please enter an amount")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) { 
                        	dialog.dismiss();
                        }
                     })
                     .show();
                }
            } else {
            	new AlertDialog.Builder(AddRecipeActivity.this)
                .setTitle("Fill in all fields")
                .setMessage("Please enter an ingredient")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) { 
                    	dialog.dismiss();
                    }
                 })
                 .show();
            }
        }
    };
    
    private OnClickListener listenerOfRemoveButton = new OnClickListener()
    {
        public void onClick(View v) {
        	int ID = ((Button)v).getId();
        	TableLayout table = (TableLayout) findViewById(R.id.IngredientsTable);
        	int ItemID = 0;
        	int difference = 0;
        	difference = counterRow - counterRemove;
        	ItemID = ID + difference;
        	TableRow row = (TableRow)findViewById(ItemID);
        	table.removeView(row);
        	row = (TableRow)findViewById(ItemID+remember);
        	table.removeView(row);
        	row = (TableRow)findViewById(ItemID+remember+1);
        	table.removeView(row);
        }
    };
    
    public void listenerOfUploadButton(View v) {
    	EditText Ingredient = (EditText)findViewById(counterIngredient);
        EditText Amount = (EditText)findViewById(counterAmount);
        EditText Method = (EditText)findViewById(R.id.MethodBox);
        EditText Name = (EditText)findViewById(R.id.NameOfRecipe);
        String IngredientString = Ingredient.getText().toString();
        String AmountString = Amount.getText().toString();
        String MethodString = Method.getText().toString();
        String NameString = Name.getText().toString();
        if ((IngredientString != null) && (IngredientString.length() > 0) &&
        		(AmountString != null) && (AmountString.length() > 0) &&
        		(MethodString != null) && (MethodString.length() > 0) &&
        		(NameString != null) && (NameString.length() > 0)) {
        	new AlertDialog.Builder(AddRecipeActivity.this)
            .setTitle("Upload Recipe?")
            .setMessage("Are you sure you want to upload recipe: "+ NameString)
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                	uploadInformation();
                	dialog.dismiss();
                }
             })
             .setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) { 
                	dialog.dismiss();
                }
             })
             .show();
        } else {
        	new AlertDialog.Builder(AddRecipeActivity.this)
        	.setTitle("Fill in all fields")
        	.setMessage("Please fill in all recipe details")
        	.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        		public void onClick(DialogInterface dialog, int which) { 
        			dialog.dismiss();
        		}
        	})
        	.show();
        }
    }
    
    public static String UppercaseFirstLetters(String str) 
    {
        boolean prevWasWhiteSp = true;
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (Character.isLetter(chars[i])) {
                if (prevWasWhiteSp) {
                    chars[i] = Character.toUpperCase(chars[i]);    
                }
                prevWasWhiteSp = false;
            } else {
                prevWasWhiteSp = Character.isWhitespace(chars[i]);
            }
        }
        return new String(chars);
    }
    
    public void uploadInformation() {
    	EditText RecipeName = (EditText)findViewById((int) getItemId(R.id.NameOfRecipe));
    	Spinner MealType = (Spinner)findViewById((int) getItemId(R.id.RecipeType));
    	EditText Hours = (EditText)findViewById((int) getItemId(R.id.Hours));
    	EditText Minutes = (EditText)findViewById((int) getItemId(R.id.Minutes));
    	EditText Season = (EditText)findViewById((int) getItemId(R.id.Season));
    	EditText Method = (EditText)findViewById((int) getItemId(R.id.MethodBox));
    	EditText Ingredient = null;
        EditText Amount = null;
        Spinner WeightMeasurement = null;
        String recipeName = UppercaseFirstLetters(RecipeName.getText().toString());
        String mealType = MealType.getSelectedItem().toString();
        int hours = Integer.parseInt(Hours.getText().toString());
        int minutes = Integer.parseInt(Minutes.getText().toString());
        int duration = minutes + (hours*60);
        String season = Season.getText().toString();
        String method = Method.getText().toString();
        String ingredient = null;
        int amount = 0;
        String weightMeasurement = null;
        Cursor ingredientCursor = null;
        int counter = 0;
        long recipeID = 0;
        long ingredientID = 0;
    	/*
        String MethodString = Method.getText().toString();
        String NameString = Name.getText().toString();*/
    	recipeID = mDbHelper.createRecipe(recipeName, method, mealType,
    			duration, season, ""/*for region when added*/);
    	for (int i = (int) getItemId(R.id.IngredientBox0); i <= counterIngredient; i+=25){
    		Ingredient = (EditText)findViewById(i);
    		Amount = (EditText)findViewById((int) getItemId(R.id.AmountBox0)+counter);
    		WeightMeasurement = (Spinner)findViewById((int) getItemId(R.id.WeightMeasurement0)+counter);
    		ingredient = UppercaseFirstLetters(Ingredient.getText().toString());
    		amount = Integer.parseInt(Amount.getText().toString());
    		weightMeasurement = WeightMeasurement.getSelectedItem().toString();
    		if(mDbHelper.fetchIngredient(ingredient).getCount() <= 0){
    			ingredientID = mDbHelper.createIngredient(ingredient);
    		} else {
    			ingredientCursor = mDbHelper.fetchIngredient(ingredient);
    			ingredientID = ingredientCursor.getLong(0);
    		}
    		if(weightMeasurement.equals("Ounce(s) (oz)")){
    			amount = (int) Math.round(amount*28.35);
    			weightMeasurement = "Grams(s) (g)";
    		} else if (weightMeasurement.equals("Pound(s) (lb)")){
    			amount = (int) Math.round(amount*0.4536);
    			weightMeasurement = "Grams(s) (g)";
    		} else if (weightMeasurement.equals("Pint(s)")){
    			amount = (int) Math.round(amount*0.5683);
    			weightMeasurement = "Litre(s) (l)";
    		} else if (weightMeasurement.equals("Gallon(s)")){
    			amount = (int) Math.round(amount*4.5461);
    			weightMeasurement = "Litre(s) (l)";
    		} else if (weightMeasurement.equals("Cup(s)")){
    			amount = (int) Math.round(amount*240);
    			weightMeasurement = "Millilitre(s) (ml)";
    		}
    		mDbHelper.createRecipeIngredient(recipeID, ingredientID, amount, weightMeasurement);
    		counter += 25;
    	}
    	startActivity(getIntent());
    	finish();
    }
}
