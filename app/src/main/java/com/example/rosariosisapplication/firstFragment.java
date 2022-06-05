package com.example.rosariosisapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.AdapterView;
import android.widget.TableRow;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link firstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class firstFragment
        extends Fragment
        implements AdapterView.OnItemSelectedListener, RecyclerViewInterface, View.OnClickListener {

    static Spinner quarterSelect, yearSelect = null;
    static Button logoutButton;
    static ArrayAdapter<CharSequence> quarterAdapter;
    static ArrayAdapter<CharSequence> yearAdapter;

    private RecyclerView recyclerView;
    private CardAdapter adapter;

    public static ArrayList<ArrayList<String>> grades = null;
    public static ArrayList<ArrayList<String>> classGrades = null;
    public static ArrayList<ArrayList<String>> zeroGrades = null;

    private ArrayList<CardHolder> classgradesArrayList;

    public static String classname;
    public static int counter = 0;

    public static String initialMarkingPeriod;
    public static String initialYear;

    public static String userName;
    public static String userPassword;

    final static String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";
    final static String LOGIN_FORM_URL = "https://rosariosis.asianhope.org/index.php";
    //rather than the grades, the initial log in action url is the portral page possibly?
    final static String GRADES_URL = "https://rosariosis.asianhope.org/Modules.php?modname=Grades/StudentGrades.php";

    public static String quarterName;
    public static String yearName;

    //Testing
    public static ArrayList<ArrayList<String>> markingperiods;
    public static ArrayList<ArrayList<String>> years;

    //for the changes in notifcations
    public static String savedGrades; //this is the variable that compares it at the end :)
    public static int backgroundCheckPeriod = 15; //this is 15 minutes and this value should be able to be changed

    private boolean isQuarterSelectTouched = false;
    private boolean isYearSelectTouched = false;

    public static boolean savedToFile = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CardView quarter1, quarter2, quarter3, quarter4;
    public TextView quarter1text, quarter2text, quarter3text, quarter4text;

    public firstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment firstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static firstFragment newInstance(String param1, String param2) {
        firstFragment fragment = new firstFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Bundle bundle = getActivity().getIntent().getExtras();
        userName = bundle.getString("username");
        userPassword = bundle.getString("userpassword");

        SharedPreferences settings = getActivity().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE); //this one is the old classGrades.toString
        //SharedPreferences settings2 = getSharedPreferences(PREFS_NAME, MODE_PRIVATE); //can be used for other needed to be saved variables
        savedGrades = settings.getString("toString classGrades", "");

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();


    }

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        counter = 0;

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        //classes = (TableLayout) getView().findViewById(R.id.main);
        //classess = (GridLayout) getView().findViewById(R.id.classes);

        quarterSelect = (Spinner) getView().findViewById(R.id.Quarters); //TODO: change "Years" into "Quarters"
        yearSelect = (Spinner) getView().findViewById(R.id.Years);
        logoutButton = (Button) getView().findViewById(R.id.btnLogout);

        recyclerView = (RecyclerView) getView().findViewById(R.id.classgradesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        quarter1text = getView().findViewById(R.id.quarter1text);
        quarter2text = getView().findViewById(R.id.quarter2text);
        quarter3text = getView().findViewById(R.id.quarter3text);
        quarter4text = getView().findViewById(R.id.quarter4text);

        quarter1 = getView().findViewById(R.id.quarter1);
        quarter2 = getView().findViewById(R.id.quarter2);
        quarter3 = getView().findViewById(R.id.quarter3);
        quarter4 = getView().findViewById(R.id.quarter4);

        quarter1.setOnClickListener(this);
        quarter2.setOnClickListener(this);
        quarter3.setOnClickListener(this);
        quarter4.setOnClickListener(this);

        quarter1.setClickable(false);
        quarter2.setClickable(false);
        quarter3.setClickable(false);
        quarter4.setClickable(false);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Logout")
                        .setMessage("Are you sure you would like to logout?")
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences preferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                                SharedPreferences settings = getActivity().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear();
                                editor.commit();
                                editor = settings.edit();
                                editor.clear();
                                editor.commit();
                                grades = null;
                                classGrades = null;
                                counter = 0;

                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        isQuarterSelectTouched = false;
        isYearSelectTouched = false;

        quarterSelect.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isQuarterSelectTouched = true;
                Log.d("onTouchListner", "Quarter select touched!");
                return false;
            }
        });

        yearSelect.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isYearSelectTouched = true;
                Log.d("onTouchListner", "Year select touched!");
                return false;
            }
        });

        if (grades != null && markingperiods!=null) {
            counter++;
            renderCards();
            selectCard();
        } else {
            description_webscrape dw = new description_webscrape(); //not sure if this part works
            dw.execute();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.Quarters:
                if (isQuarterSelectTouched == true) {
                    quarterName = parent.getItemAtPosition(position).toString(); //"Quarter 1", "Quarter 2", etc
                    description_webscrape dw = new description_webscrape(); //not sure if this part works
                    dw.execute();
                }
                break;
            case R.id.Years:
                if (isYearSelectTouched == true) {
                    yearName = parent.getItemAtPosition(position).toString();
                    description_webscrape dw = new description_webscrape(); //not sure if this part works
                    dw.execute();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), AssignmentGrades.class);

        intent.putExtra("classname", classgradesArrayList.get(position).getClassname2());
        intent.putExtra("classpercentage", classgradesArrayList.get(position).getPercentage2());

        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        description_webscrape dw = new description_webscrape(); //not sure if this part works
        switch(v.getId()){
            case R.id.quarter1:
                quarterName = "Quarter 1";//"Quarter 1", "Quarter 2", etc

                quarter1.setCardBackgroundColor(Color.parseColor("#5566e7"));
                quarter2.setCardBackgroundColor(Color.WHITE);
                quarter3.setCardBackgroundColor(Color.WHITE);
                quarter4.setCardBackgroundColor(Color.WHITE);

                quarter1text.setTextColor(Color.WHITE);
                quarter2text.setTextColor(Color.parseColor("#5566e7"));
                quarter3text.setTextColor(Color.parseColor("#5566e7"));
                quarter4text.setTextColor(Color.parseColor("#5566e7"));

                quarter1.setClickable(false);
                quarter2.setClickable(true);
                quarter3.setClickable(true);
                quarter4.setClickable(true);

                dw.execute();
                break;

            case R.id.quarter2:
                quarterName = "Quarter 2";//"Quarter 1", "Quarter 2", etc

                quarter1.setCardBackgroundColor(Color.WHITE);
                quarter2.setCardBackgroundColor(Color.parseColor("#5566e7"));
                quarter3.setCardBackgroundColor(Color.WHITE);
                quarter4.setCardBackgroundColor(Color.WHITE);

                quarter1text.setTextColor(Color.parseColor("#5566e7"));
                quarter2text.setTextColor(Color.WHITE);
                quarter3text.setTextColor(Color.parseColor("#5566e7"));
                quarter4text.setTextColor(Color.parseColor("#5566e7"));

                quarter1.setClickable(true);
                quarter2.setClickable(false);
                quarter3.setClickable(true);
                quarter4.setClickable(true);

                dw.execute();
                break;

            case R.id.quarter3:
                quarterName = "Quarter 3";//"Quarter 1", "Quarter 2", etc

                quarter1.setCardBackgroundColor(Color.WHITE);
                quarter2.setCardBackgroundColor(Color.WHITE);
                quarter3.setCardBackgroundColor(Color.parseColor("#5566e7"));
                quarter4.setCardBackgroundColor(Color.WHITE);

                quarter1text.setTextColor(Color.parseColor("#5566e7"));
                quarter2text.setTextColor(Color.parseColor("#5566e7"));
                quarter3text.setTextColor(Color.WHITE);
                quarter4text.setTextColor(Color.parseColor("#5566e7"));

                quarter1.setClickable(true);
                quarter2.setClickable(true);
                quarter3.setClickable(false);
                quarter4.setClickable(true);

                dw.execute();
                break;
            case R.id.quarter4:
                quarterName = "Quarter 4";//"Quarter 1", "Quarter 2", etc

                quarter1.setCardBackgroundColor(Color.WHITE);
                quarter2.setCardBackgroundColor(Color.WHITE);
                quarter3.setCardBackgroundColor(Color.WHITE);
                quarter4.setCardBackgroundColor(Color.parseColor("#5566e7"));

                quarter1text.setTextColor(Color.parseColor("#5566e7"));
                quarter2text.setTextColor(Color.parseColor("#5566e7"));
                quarter3text.setTextColor(Color.parseColor("#5566e7"));
                quarter4text.setTextColor(Color.WHITE);

                quarter1.setClickable(true);
                quarter2.setClickable(true);
                quarter3.setClickable(true);
                quarter4.setClickable(false);

                dw.execute();
                break;
        }
    }

    class description_webscrape extends AsyncTask<Void, Void, Void> {

        private ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            jsoupScraper();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            renderCards();
            if (counter <= 1) {
                quarterName = initialMarkingPeriod;
                yearName = initialYear;
                //periodicWork();
                selectCard();
            }
        }
    }

    public static void jsoupScraper () {

        try {

            grades = new ArrayList<ArrayList<String>>();
            classGrades = new ArrayList<ArrayList<String>>();
            zeroGrades = new ArrayList<ArrayList<String>>();

            String mp = null;
            String yr = null;

            Connection.Response loginForm = Jsoup.connect(LOGIN_FORM_URL)
                    .method(Connection.Method.GET)
                    .userAgent(USER_AGENT)
                    .execute();

            loginForm = Jsoup.connect(LOGIN_FORM_URL)
                    .cookies(loginForm.cookies())
                    .data("USERNAME", userName)
                    .data("PASSWORD", userPassword)
                    .method(Connection.Method.POST)
                    .followRedirects(true)
                    .userAgent(USER_AGENT)
                    .execute();

            Document doc = Jsoup.connect(GRADES_URL)
                    .cookies(loginForm.cookies())
                    .userAgent(USER_AGENT)
                    .get();

            if (counter < 1) {
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
                markingperiods = new ArrayList<ArrayList<String>>();

                for(int i=0; i<years.size();i++){
                    if(years.get(i).get(0).equals(yearName)){
                        yr = years.get(i).get(1);
                    }
                }

                if(yr != null) {
                    Log.d("test", "year update successful");
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
                        Log.d("test", quarterName);
                        Log.d("test", mp);
                    }
                }

                if(mp != null && yr != null) {
                    Log.d("test", "year and mp update successful");
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

            org.jsoup.nodes.Element table = null;

            if(doc.select("table[class=list widefat rt]").isEmpty()){

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

                    if(counter == 1 && temp1.size() > 0){
                        if (temp1.get(4).equals("0.0%") || temp1.get(4).equals("*")) {
                            zeroGrades.add(temp1);
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
        Log.d("notiftest", "Grades pulled");

        /*
        for (int i = 0; i< zeroGrades.size();i++) {
            Log.d("zeroGrades", String.valueOf(zeroGrades.get(i)));
        }
             */

    }


    public void renderCards() {
        classgradesArrayList = new ArrayList<>();
        CardHolder temp;

        for (int i = 0; i < grades.size(); i++) {
            temp = new CardHolder(grades.get(i).get(0), grades.get(i).get(1), letterGrade(grades.get(i).get(2)), grades.get(i).get(2));
            classgradesArrayList.add(temp);
        }

        adapter = new CardAdapter(this.getActivity(), classgradesArrayList, this);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    public static void periodicWork() {
        Log.e("timer", "periodicWork: time starts now");
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
                periodicWork.class, backgroundCheckPeriod, TimeUnit.MINUTES).setInitialDelay(backgroundCheckPeriod, TimeUnit.MINUTES).build();
        WorkManager.getInstance().enqueueUniquePeriodicWork("PWR",ExistingPeriodicWorkPolicy.KEEP, periodicWorkRequest);
    }

    public String letterGrade (String percentageGrade){

        if (percentageGrade == "*") {
            return "*";
        }

        String percentageString = percentageGrade.substring(0, percentageGrade.length() - 1);
        String letterGrade = "";
        double percentageDouble = Double.parseDouble(percentageString);
        int percentage = (int) Math.round(percentageDouble);

        Log.d("lettergrade", String.valueOf(percentage));

        if(percentage<60){
            letterGrade="F";
        }
        else if(percentage<63){
            letterGrade="D-";
        }
        else if(percentage<67){
            letterGrade="D";
        }
        else if(percentage<70){
            letterGrade="D+";
        }
        else if(percentage<73){
            letterGrade="C-";
        }
        else if(percentage<77){
            letterGrade="C";
        }
        else if(percentage<80){
            letterGrade="C+";
        }
        else if(percentage<83){
            letterGrade="B-";
        }
        else if(percentage<87){
            letterGrade="B";
        }
        else if(percentage<90){
            letterGrade="B+";
        }
        else if(percentage<93){
            letterGrade="A-";
        }
        else if(percentage<97){
            letterGrade="A";
        }
        else if(percentage<=100){
            letterGrade="A+";
        }
        else{
            letterGrade="ERROR";
        }

        return letterGrade;
    }

    void selectCard() {

        if (quarterName.equals("Quarter 1")) {

            quarter1.setCardBackgroundColor(Color.parseColor("#5566e7"));
            quarter1text.setTextColor(Color.WHITE);
            quarter2.setClickable(true);
            quarter3.setClickable(true);
            quarter4.setClickable(true);

        } else if (quarterName.equals("Quarter 2")) {

            quarter2.setCardBackgroundColor(Color.parseColor("#5566e7"));
            quarter2text.setTextColor(Color.WHITE);
            quarter1.setClickable(true);
            quarter3.setClickable(true);
            quarter4.setClickable(true);

        } else if (quarterName.equals("Quarter 3")) {

            quarter3.setCardBackgroundColor(Color.parseColor("#5566e7"));
            quarter3text.setTextColor(Color.WHITE);
            quarter1.setClickable(true);
            quarter2.setClickable(true);
            quarter4.setClickable(true);

        } else if (quarterName.equals("Quarter 4")) {

            quarter4.setCardBackgroundColor(Color.parseColor("#5566e7"));
            quarter4text.setTextColor(Color.WHITE);
            quarter1.setClickable(true);
            quarter2.setClickable(true);
            quarter3.setClickable(true);

        }
    }

}