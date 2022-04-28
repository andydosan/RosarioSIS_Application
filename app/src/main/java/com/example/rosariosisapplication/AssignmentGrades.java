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

    TextView ClassNameTextView, ClassPercentageTextView;

    RecyclerView recyclerView;

    private ArrayList<CardHolder2> assignmentgradesArrayList;
    private CardAdapter2 adapter;

    String classname;
    String classpercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_grades);

        ClassNameTextView = findViewById(R.id.classnametextview);
        ClassPercentageTextView = findViewById(R.id.classpercentagetextview);

        recyclerView = (RecyclerView) findViewById(R.id.assignmentgradesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Bundle bundle = this.getIntent().getExtras();
        classname = bundle.getString("classname");
        classpercentage = bundle.getString("classpercentage");

        ClassNameTextView.setText(classname);
        ClassPercentageTextView.setText(classpercentage);

        assignmentgradesArrayList = new ArrayList<>();
        CardHolder2 temp;

        for (int i = 0; i < firstFragment.classGrades.size(); i++) {
            if (firstFragment.classGrades.get(i).get(0).equals(classname)) {
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


    }
}