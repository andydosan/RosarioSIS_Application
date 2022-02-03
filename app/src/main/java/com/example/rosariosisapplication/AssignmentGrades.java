package com.example.rosariosisapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class AssignmentGrades extends AppCompatActivity {

    TableLayout assignmentGrades;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_grades);

        assignmentGrades = (TableLayout) findViewById(R.id.assignmentsMain);
        Log.d("Myapp", HomePageActivity.classname);
        Log.d("Myapp", String.valueOf(HomePageActivity.classGrades.size()));

        for (int i = 0; i < HomePageActivity.classGrades.size(); i++) {

            Log.d("Myapp", String.valueOf(HomePageActivity.classGrades.get(i)));
            if (HomePageActivity.classGrades.get(i).get(0).equals(HomePageActivity.classname)) {
                TableRow tbrow0 = new TableRow(AssignmentGrades.this);
                tbrow0.setMinimumHeight(200);

                TextView tv0 = new TextView(AssignmentGrades.this);
                TextView tv1 = new TextView(AssignmentGrades.this);
                TextView tv2 = new TextView(AssignmentGrades.this);
                tv0.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
                tv1.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
                tv2.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));

                tv0.setGravity(Gravity.CENTER);
                tv1.setGravity(Gravity.CENTER);
                tv2.setGravity(Gravity.CENTER);

                tv0.setWidth(1500);
                tv1.setWidth(1500);
                tv2.setWidth(1500);

                tv0.setText(HomePageActivity.classGrades.get(i).get(1));
                tv1.setText(HomePageActivity.classGrades.get(i).get(3));
                tv2.setText(HomePageActivity.classGrades.get(i).get(4));

                tbrow0.addView(tv0);
                tbrow0.addView(tv1);
                tbrow0.addView(tv2);

                assignmentGrades.addView(tbrow0);
            }
        }
    }
}