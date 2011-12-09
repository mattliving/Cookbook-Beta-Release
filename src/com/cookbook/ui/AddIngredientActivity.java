package com.cookbook.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;

import com.cookbook.*;

public class AddIngredientActivity extends Activity {
	int counterIngredient = 0x7f070002;
	int counterAmount = 0x7f070005;
	int counterMeasurement = 0x7f070007;
	int counterEdit = 0x7f070003;
	LayoutParams param = new LinearLayout.LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT, (float) 1.0);
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_ingredient);
        
        Button mainNext = (Button) findViewById(R.id.AddIngredients);
        mainNext.setOnClickListener(new OnClickListener() {
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
                		IngredientED.setFocusable(false);
                		AmountED.setFocusable(false);
                		MeasurementS.setFocusable(false);
                		IngredientED.setTextColor(Color.LTGRAY);
                		AmountED.setTextColor(Color.LTGRAY);
                		ButtonEdit.setVisibility(View.VISIBLE);
                		counterIngredient += 16;
                		counterAmount += 16;
                		counterMeasurement += 16;
                		counterEdit += 16;
                		TableLayout table = (TableLayout) findViewById(R.id.IngredientsTable);
                		
                		TableRow rowIngredient = new TableRow(AddIngredientActivity.this);
                		EditText tIngredient = new EditText(AddIngredientActivity.this);
                		tIngredient.setHint("Ingredient");
                		tIngredient.setInputType(1);
                		tIngredient.setId(counterIngredient);
                		Button editButton = new Button(AddIngredientActivity.this);
                		editButton.setVisibility(View.INVISIBLE);
                		editButton.setText("Edit");
                		editButton.setId(counterEdit);
                		rowIngredient.addView(tIngredient);
                		rowIngredient.addView(editButton);
                		table.addView(rowIngredient,new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, (float) 1));
                		
                		TableRow rowAmount = new TableRow(AddIngredientActivity.this);
                		EditText tAmount = new EditText(AddIngredientActivity.this);
                		tAmount.setHint("Amount (weight, volume etc.)");
                		tAmount.setInputType(0);
                		tAmount.setId(counterAmount);
                		rowAmount.addView(tAmount);
                		table.addView(rowAmount,new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, (float) 1));
                		
                		TableRow rowMeasurement = new TableRow(AddIngredientActivity.this);
                		Spinner tMeasurement = new Spinner(AddIngredientActivity.this);
                		tMeasurement.setPrompt("Measurement");
                		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                		AddIngredientActivity.this, R.array.WeightMeasurement, android.R.layout.simple_spinner_item);
                		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                	    tMeasurement.setAdapter(adapter);
                		tMeasurement.setId(counterMeasurement);
                		rowMeasurement.addView(tMeasurement);
                		table.addView(rowMeasurement,new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, (float) 1));
                    } else {
                    	new AlertDialog.Builder(AddIngredientActivity.this)
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
                	new AlertDialog.Builder(AddIngredientActivity.this)
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

}
