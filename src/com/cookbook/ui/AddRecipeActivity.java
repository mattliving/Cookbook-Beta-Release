package com.cookbook.ui;

import com.cookbook.R;
import com.cookbook.R.array;
import com.cookbook.R.drawable;
import com.cookbook.R.id;
import com.cookbook.R.layout;
import com.cookbook.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TableLayout;
import android.widget.TableRow;

public class AddRecipeActivity extends Activity {
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //Initialising variables
        TableRow rowAmount = (TableRow)findViewById(R.id.Amount1);
        TableRow rowMeasurement = (TableRow)findViewById(R.id.Measurement1);
        rowAmount.setId(counterRow+remember);
        rowMeasurement.setId(counterRow+remember+1);
        
        
        TabHost tabHost=(TabHost)findViewById(R.id.tabHost);
        tabHost.setup();

        TabSpec spec1=tabHost.newTabSpec("Tab 1");
        spec1.setIndicator("Tab 1",getResources().getDrawable(R.drawable.ic_launcher));
        spec1.setContent(R.id.tab1);

        TabSpec spec2=tabHost.newTabSpec("Tab 2");
        spec2.setIndicator("Tab 2",getResources().getDrawable(R.drawable.ic_launcher));
        spec2.setContent(R.id.tab2);

        TabSpec spec3=tabHost.newTabSpec("Tab 3");
        spec3.setIndicator("Tab 3",getResources().getDrawable(R.drawable.ic_launcher));
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
                		tIngredient.setInputType(1);
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
                		tAmount.setInputType(0);
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
        
    private OnClickListener listenerOfEditButton = new OnClickListener()
    {
        @Override
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
        @Override
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
        @Override
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
    public void uploadInformation() {
    	//upload shit!
    }
}