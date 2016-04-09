package com.justinross.caloriescounter;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.justinross.caloriecounter.R;

import data.DatabaseHandler;
import model.Food;

public class FoodItemDetails extends AppCompatActivity {

    private TextView foodName, calories, dateTaken;
    private Button shareButton;
    private int foodId;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item_details);

        foodName = (TextView) findViewById(R.id.detsFoodName);
        calories = (TextView) findViewById(R.id.detscaloriesValue);
        dateTaken = (TextView) findViewById(R.id.detsDateText);
        shareButton = (Button) findViewById(R.id.detsShareButton);

        deleteButton = (Button) findViewById(R.id.deleteButton);



        Food food = (Food) getIntent().getSerializableExtra("userObj");

        foodName.setText(food.getFoodName());
        calories.setText(String.valueOf(food.getCalories()));
        dateTaken.setText(food.getRecordDate());


        foodId = food.getFoodId();

        calories.setTextSize(34.9f);
        calories.setTextColor(Color.RED);


        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareCals();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: put delete functionality here
                AlertDialog.Builder alert = new AlertDialog.Builder(FoodItemDetails.this);
                alert.setTitle("Delete?");
                alert.setMessage("Are you sure you want to delete this item?");
                alert.setNegativeButton("No", null);
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DatabaseHandler dba = new DatabaseHandler(getApplicationContext());
                        dba.deleteFood(foodId);

                        Toast.makeText(FoodItemDetails.this, "Food Item Deleted!", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(FoodItemDetails.this, DisplayFoodsActivity.class));

                        //remove this activity from activity stack
                        FoodItemDetails.this.finish();

                    }
                });

                alert.show();

            }
        });


    }





    public void shareCals() {

        StringBuilder dataString = new StringBuilder();

        String name = foodName.getText().toString();
        String cals = calories.getText().toString();
        String date = dateTaken.getText().toString();

        dataString.append(" Food: " + name + "\n");
        dataString.append(" Calories: " + cals + "\n");
        dataString.append(" Eaten on: " + date);

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_SUBJECT, "My Caloric Intake");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"cloudvizion@outlook.com"});
        i.putExtra(Intent.EXTRA_TEXT, dataString.toString());


        try {

            startActivity(Intent.createChooser(i, "send mail..."));

        }catch (ActivityNotFoundException e){
            Toast.makeText(FoodItemDetails.this, "Please install email client before sending", Toast.LENGTH_SHORT).show();
        }
    }
}
