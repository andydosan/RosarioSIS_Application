package com.example.rosariosisapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    RecyclerView recyclerView;

    private ArrayList<CardHolder2> assignmentgradesArrayList;
    private CardAdapter2 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_grades);

        assignmentGrades = (TableLayout) findViewById(R.id.assignmentsMain);
        recyclerView = (RecyclerView) findViewById(R.id.assignmentgradesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        assignmentgradesArrayList = new ArrayList<>();
        CardHolder2 temp;

        for (int i = 0; i < firstFragment.classGrades.size(); i++) {
            if (firstFragment.classGrades.get(i).get(0).equals(firstFragment.classname)) {
                temp = new CardHolder2(
                        firstFragment.classGrades.get(i).get(1),
                        firstFragment.classGrades.get(i).get(2),
                        firstFragment.classGrades.get(i).get(3),
                        firstFragment.classGrades.get(i).get(4));
                assignmentgradesArrayList.add(temp);
            }

        }

        adapter = new CardAdapter2(this, assignmentgradesArrayList);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        /*
        for (int i = 0; i < firstFragment.classGrades.size(); i++) {

            Log.d("Myapp", String.valueOf(firstFragment.classGrades.get(i)));
            if (firstFragment.classGrades.get(i).get(0).equals(firstFragment.classname)) {
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

                tv0.setText(firstFragment.classGrades.get(i).get(1));
                tv1.setText(firstFragment.classGrades.get(i).get(3));
                tv2.setText(firstFragment.classGrades.get(i).get(4));

                tbrow0.addView(tv0);
                tbrow0.addView(tv1);
                tbrow0.addView(tv2);

                assignmentGrades.addView(tbrow0);
            }
        }
         */
    }
}