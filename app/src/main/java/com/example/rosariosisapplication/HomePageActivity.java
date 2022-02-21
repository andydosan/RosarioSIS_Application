package com.example.rosariosisapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.UncheckedIOException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.SocketException;
import java.util.Map;

public class HomePageActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TableLayout classes;
    Spinner quarterSelect, yearSelect = null;
    ArrayAdapter<CharSequence> quarterAdapter;
    ArrayAdapter<CharSequence> yearAdapter;

    //NOTE: password is a RosarioSis password stored in strings.xml. DO NOT OPEN STRINGS.XML!
    String password;
    public static ArrayList<ArrayList<String>> grades = new ArrayList<ArrayList<String>>();
    public static ArrayList<ArrayList<String>> classGrades = new ArrayList<ArrayList<String>>();
    public static String classname;
    public int counter=0;

    String initialMarkingPeriod;
    String initialYear;

    final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";
    final String LOGIN_FORM_URL = "https://rosariosis.asianhope.org/index.php";
    //rather than the grades, the initial log in action url is the portral page possibly?
    final String LOGIN_ACTION_URL = "https://rosariosis.asianhope.org/Modules.php?modname=misc/Portal.php";
    final String GRADES_URL = "https://rosariosis.asianhope.org/Modules.php?modname=Grades/StudentGrades.php";

    private static final String FILE_NAME = "gradeData.txt";

    //Form data for each quarter
    public String[][] formData = {
            {"Quarter 1", "70"},
            {"Quarter 2", "73"},
            {"Quarter 3", "75"},
            {"Quarter 4", "72"},
    };

    public String quarterName;
    public String yearName;

    //Testing
    public ArrayList<ArrayList<String>> markingperiods;
    public ArrayList<ArrayList<String>> years;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        password = getResources().getString(R.string.andy_password);
        password = getString(R.string.andy_password);

        classes = (TableLayout) findViewById(R.id.main);

        quarterSelect = (Spinner) findViewById(R.id.Quarters); //TODO: change "Years" into "Quarters"
        yearSelect = (Spinner) findViewById(R.id.Years);
        //quarterAdapter = ArrayAdapter.createFromResource(this, R.array.quarters, android.R.layout.simple_spinner_item);
        //quarterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //quarterSelect.setAdapter(quarterAdapter);

        description_webscrape dw = new description_webscrape(); //not sure if this part works
        dw.execute();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.Quarters:
                quarterName = parent.getItemAtPosition(position).toString(); //"Quarter 1", "Quarter 2", etc

                description_webscrape dw = new description_webscrape(); //not sure if this part works
                dw.execute();
                break;
            case R.id.Years:
                yearName = parent.getItemAtPosition(position).toString();

                dw = new description_webscrape(); //not sure if this part works
                dw.execute();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class description_webscrape extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {

                grades = new ArrayList<ArrayList<String>>();
                classGrades = new ArrayList<ArrayList<String>>();

                String mp = null;
                String yr = null;

                Connection.Response loginForm = Jsoup.connect(LOGIN_FORM_URL)
                        .method(Connection.Method.GET)
                        .userAgent(USER_AGENT)
                        .execute();

                loginForm = Jsoup.connect(LOGIN_FORM_URL)
                        .cookies(loginForm.cookies())
                        .data("USERNAME", "adosan")
                        .data("PASSWORD", password)
                        .method(Connection.Method.POST)
                        .followRedirects(true)
                        .userAgent(USER_AGENT)
                        .execute();

                Document doc = Jsoup.connect(GRADES_URL)
                        .cookies(loginForm.cookies())
                        .userAgent(USER_AGENT)
                        .get();

                Log.d("asdf", String.valueOf(counter));

                if (counter < 1) {
                    Log.d("asdf", "GOING INTO IF");
                    markingperiods = new ArrayList<ArrayList<String>>();
                    years = new ArrayList<ArrayList<String>>();

                    Elements selectedyear = doc.select("select#syear option[selected]");
                    Elements selectedmp = doc.select("select#mp option[selected]");

                    // Marking periods and years
                    Elements syearselector = doc.select("select#syear");
                    Elements mpselector = doc.select("select#mp");
                    syearselector = syearselector.select("option");
                    mpselector = mpselector.select("option");

                    for (int i = 0; i < syearselector.size(); i++) {
                        ArrayList<String> temp = new ArrayList<String>();
                        temp.add(syearselector.get(i).text());
                        temp.add(syearselector.get(i).val());
                        years.add(temp);
                    }

                    for (int i = 0; i < mpselector.size(); i++) {
                        ArrayList<String> temp = new ArrayList<String>();
                        temp.add(mpselector.get(i).text());
                        temp.add(mpselector.get(i).val());
                        markingperiods.add(temp);
                    }

                    Connection.Response quarter = Jsoup.connect("https://rosariosis.asianhope.org/Side.php?sidefunc=update")
                            .cookies(loginForm.cookies())
                            .data("syear", selectedyear.val())
                            .data("mp", selectedmp.val())
                            .method(Connection.Method.POST)
                            .followRedirects(true)
                            .userAgent(USER_AGENT)
                            .execute();

                    initialMarkingPeriod = selectedmp.text();
                    initialYear = selectedyear.text();

                    doc = Jsoup.connect(GRADES_URL)
                            .cookies(loginForm.cookies())
                            .userAgent(USER_AGENT)
                            .get();
                } else {
                    Log.d("asdf", "GOING INTO ELSE");
                    markingperiods = new ArrayList<ArrayList<String>>();

                    for(int i=0; i<years.size();i++){
                        if(years.get(i).get(0).equals(yearName)){
                            yr = years.get(i).get(1);
                        }
                    }

                    if(yr != null) {
                        Connection.Response update = Jsoup.connect("https://rosariosis.asianhope.org/Side.php?sidefunc=update")
                                .cookies(loginForm.cookies())
                                .data("syear", yr)
                                .method(Connection.Method.POST)
                                .followRedirects(true)
                                .userAgent(USER_AGENT)
                                .execute();
                    }

                    doc = Jsoup.connect(GRADES_URL)
                            .cookies(loginForm.cookies())
                            .userAgent(USER_AGENT)
                            .get();

                    // Marking periods and years
                    Elements mpselector = doc.select("select#mp");
                    mpselector = mpselector.select("option");

                    for (int i = 0; i < mpselector.size(); i++) {
                        ArrayList<String> temp = new ArrayList<String>();
                        temp.add(mpselector.get(i).text());
                        temp.add(mpselector.get(i).val());
                        markingperiods.add(temp);
                    }

                    for(int i=0; i<markingperiods.size();i++){
                        if(markingperiods.get(i).get(0).equals(quarterName)){
                            mp = markingperiods.get(i).get(1);
                        }
                    }

                    if(mp != null && yr != null) {
                        Connection.Response update = Jsoup.connect("https://rosariosis.asianhope.org/Side.php?sidefunc=update")
                                .cookies(loginForm.cookies())
                                .data("syear", yr)
                                .data("mp", mp)
                                .method(Connection.Method.POST)
                                .followRedirects(true)
                                .userAgent(USER_AGENT)
                                .execute();
                    }

                    doc = Jsoup.connect(GRADES_URL)
                            .cookies(loginForm.cookies())
                            .userAgent(USER_AGENT)
                            .get();
                }
                counter++;
                Log.d("asdfg", String.valueOf(markingperiods));

                org.jsoup.nodes.Element table;
                if(doc.select("table[class=list widefat rt]").isEmpty()){
                    return null;
                } else {
                   table = doc.select("table[class=list widefat rt]").get(0);
                }
                Elements rows = table.select("tr");

                for (int i = 1; i < rows.size(); i++) { //first row is the col names so skip it.
                    org.jsoup.nodes.Element row = rows.get(i);
                    Elements cols = row.select("td");
                    Elements link = row.select("a[href]");
                    String classLink = "https://rosariosis.asianhope.org/" + link.get(0).attr("href");

                    ArrayList<String> temp = new ArrayList<String>();
                    temp.add(cols.get(0).text()); //Class name
                    temp.add(cols.get(1).text()); //Teacher name
                    temp.add(cols.get(3).text()); //Grade (percent)

                    temp.add(classLink);
                    grades.add(temp);

                    org.jsoup.nodes.Document doc1 = Jsoup.connect(classLink)
                            .cookies(loginForm.cookies())
                            .userAgent(USER_AGENT)
                            .get();

                    org.jsoup.nodes.Element table1 = doc1.select("table[class=list widefat rt]").get(0);
                    doc1.select(".header2").remove();
                    doc1.select(".header2 align-right").remove();

                    Elements rows1 = table1.select("tr");
                    for (int j = 1; j < rows1.size(); j++) {

                        Element row1 = rows1.get(j);
                        Elements cols1 = row1.select("td");

                        ArrayList<String> temp1 = new ArrayList<String>();

                        if (cols1.size() > 0) {
                            temp1.add(cols.get(0).text());
                            temp1.add(cols1.get(0).text()); //Assignment Name
                            temp1.add(cols1.get(1).text()); //Assignment Category
                            temp1.add(cols1.get(2).text()); //Points / Possible
                            temp1.add(cols1.get(3).text()); //Grade (percent)

                            //Comment
                            if (cols1.get(4).text().length() > 0) {
                                temp1.add(cols1.get(4).text());
                            } else {
                                temp1.add ("No comment");
                            }
                        }

                        if (temp1.size() > 0) {
                            classGrades.add(temp1);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //TESTING PURPOSES, PLS DONT DELETE YET
            Log.d("notiftest", String.valueOf(classGrades));

            /*
            for (int i = 0; i< classGrades.size();i++){
                for (int j = 0; j< classGrades.get(i).size();j++){
                    Log.d("classgrades", classGrades.get(i).get(j));
                }
            }
             */

            FileOutputStream fos = null;
            try {
                fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                fos.write(classGrades.toString().getBytes(StandardCharsets.UTF_8));
                Log.d("testsavedtxt", getFilesDir() + "/" + FILE_NAME);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null){
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            FileInputStream fis = null;
            try {
                fis = openFileInput(FILE_NAME);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String text;
                while ((text = br.readLine()) != null) {
                    sb.append(text).append("\n");
                }
                Log.d("testsavedtxt", sb.toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
            e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("counter", String.valueOf(counter));

            if (counter == 1) {
                ArrayList<CharSequence> temp = new ArrayList<CharSequence>();
                for (int i = 0; i < markingperiods.size(); i++) {
                    temp.add(markingperiods.get(i).get(0));
                }
                quarterAdapter = new ArrayAdapter<CharSequence>(HomePageActivity.this, android.R.layout.simple_list_item_1, temp);
                quarterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                quarterSelect.setAdapter(quarterAdapter);
                quarterSelect.setSelection(quarterAdapter.getPosition(initialMarkingPeriod));
                quarterSelect.setOnItemSelectedListener(HomePageActivity.this);

                temp = new ArrayList<CharSequence>();
                for (int i = 0; i < years.size(); i++) {
                    temp.add(years.get(i).get(0));
                }

                yearAdapter = new ArrayAdapter<CharSequence>(HomePageActivity.this, android.R.layout.simple_list_item_1, temp);
                yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                yearSelect.setAdapter(yearAdapter);
                yearSelect.setSelection(yearAdapter.getPosition(initialYear));
                yearSelect.setOnItemSelectedListener(HomePageActivity.this);
            }

            //Bru
            if (counter >= 3) {
                // Remove all rows except the first one
                classes.removeViews(1, Math.max(0, classes.getChildCount() - 1));
                classes.invalidate();

                if (grades.size() == 0) {
                    //TODO: proper formatting
                    TableRow tbrow0 = new TableRow(HomePageActivity.this);
                    tbrow0.setMinimumHeight(200);

                    TableRow.LayoutParams params = new TableRow.LayoutParams();
                    params.span = 3;

                    TextView tv0 = new TextView(HomePageActivity.this);
                    tv0.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
                    tv0.setGravity(Gravity.CENTER);
                    tv0.setWidth(1500);
                    tv0.setText("There are no grades for this quarter");

                    tbrow0.addView(tv0, params);
                    classes.addView(tbrow0);
                } else {
                    for (int i = 0; i < grades.size(); i++) {
                        TableRow tbrow0 = new TableRow(HomePageActivity.this);
                        tbrow0.setMinimumHeight(200);

                        TextView tv0 = new TextView(HomePageActivity.this);
                        TextView tv1 = new TextView(HomePageActivity.this);
                        TextView tv2 = new TextView(HomePageActivity.this);
                        tv0.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
                        tv1.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
                        tv2.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));

                        tv0.setGravity(Gravity.CENTER);
                        tv1.setGravity(Gravity.CENTER);
                        tv2.setGravity(Gravity.CENTER);

                        tv0.setWidth(1500);
                        tv1.setWidth(1500);
                        tv2.setWidth(1500);

                        String temp = grades.get(i).get(0);

                        tv0.setText(temp);
                        tv0.setClickable(true);
                        tv1.setText(grades.get(i).get(1));
                        tv2.setText(grades.get(i).get(2));

                        tv0.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                classname = temp;
                                startActivity(new Intent(HomePageActivity.this, AssignmentGrades.class));
                            }
                        });

                        tbrow0.addView(tv0);
                        tbrow0.addView(tv1);
                        tbrow0.addView(tv2);

                        classes.addView(tbrow0);
                    }
                }
            }
        }
    }
}